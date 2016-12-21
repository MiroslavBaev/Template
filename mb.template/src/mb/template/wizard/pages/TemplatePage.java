package mb.template.wizard.pages;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import mb.template.dialog.FolderSelectionDialog;
import mb.template.listeners.IChangeValueListener;
import mb.template.listeners.IClickListener;
import mb.template.manager.FileManager;
import mb.template.manager.PlaceholderManager;
import mb.template.manager.ProjectManager;
import mb.template.manager.TemplateManager;
import mb.template.placeholder.PlaceholderBean;
import mb.template.placeholder.PlaceholderContainerBean;
import mb.template.storages.Template;
import mb.template.storages.TemplatesStorage;
import mb.template.validator.Validator;
import mb.template.wizard.table.editor.ColumnEditingSupport;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;


/**
 * Wizard page. Select a directory where the template to be copied
 * 
 * @author mbaev
 *
 */
public class TemplatePage extends WizardPage
{
    // private final static String TEMPLATE_PATH_PREFERENCE = "templatePath";

    private Composite container;
    private TableViewer viewer;
    private Text txtProjectFolder;
    private ComboViewer comboViewer;

    private String projectFolderPath;

    private PlaceholderContainerBean placeholderContainer;

    private TemplatesStorage templatesStorage;
    private Template selectedTemplate;



    public TemplatePage()
    {
        super("Select project");
        setTitle("Template");
        setDescription("Create a new template");

        this.placeholderContainer = new PlaceholderContainerBean();
        this.projectFolderPath = ProjectManager.getSelectedElementPath();

        this.templatesStorage = new TemplatesStorage();
        this.selectedTemplate = null;
    }



    @Override
    public void createControl(Composite parent)
    {
        this.container = new Composite(parent, SWT.NONE);
        this.container.setLayout(new GridLayout(3, false));
        //
        Label lblTemplateSourceFolder = new Label(container, SWT.NONE);
        lblTemplateSourceFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTemplateSourceFolder.setText("Template source folder");
        //
        comboViewer = new ComboViewer(container, SWT.READ_ONLY | SWT.V_SCROLL);
        Combo combo = comboViewer.getCombo();
        combo.setVisibleItemCount(8);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        // Delete combo items
        combo.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == SWT.DEL))
                {
                    templatesStorage.removePath(combo.getSelectionIndex());

                    setComboTemplates();

                    placeholderContainer.clear();
                }
            }

        });
        combo.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setSelectedProject();

                researchTemplatesForPlacehodlers();
            }

        });
        //
        comboViewer.setContentProvider(ArrayContentProvider.getInstance());
        comboViewer.setLabelProvider(new LabelProvider()
        {
            @Override
            public String getText(Object element)
            {
                if (element instanceof Template)
                {
                    Template template = (Template) element;
                    FileManager fileManager = new FileManager();

                    return fileManager.getParentFolderNameFromFullPath(template.getPath());
                }
                return super.getText(element);
            }
        });
        //
        Button btnTemplateSourceFolderBrowse = new Button(container, SWT.NONE);
        btnTemplateSourceFolderBrowse.setText("Browse..");
        //
        Label lblSeparator = new Label(container, SWT.NONE | SWT.SEPARATOR | SWT.HORIZONTAL);
        lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
        //
        Label lblProjectFolder = new Label(container, SWT.NONE);
        lblProjectFolder.setText("Project source folder");
        //
        txtProjectFolder = new Text(container, SWT.BORDER);
        txtProjectFolder.setEditable(false);
        txtProjectFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        //
        Button btnProjectFolder = new Button(container, SWT.NONE);
        btnProjectFolder.setText("Browse..");
        //
        lblSeparator = new Label(container, SWT.NONE | SWT.SEPARATOR | SWT.HORIZONTAL);
        lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
        //
        this.viewer = new TableViewer(this.container, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

        Table table = this.viewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

        // Create column
        TableViewerColumn column = new TableViewerColumn(this.viewer, SWT.NONE);
        column.getColumn().setWidth(271);
        column.getColumn().setText("Placeholder");

        column = new TableViewerColumn(this.viewer, SWT.NONE);
        column.setEditingSupport(new ColumnEditingSupport(this.viewer));
        column.getColumn().setWidth(287);
        column.getColumn().setText("Value");

        this.viewer.getTable().setHeaderVisible(true);

        btnTemplateSourceFolderBrowse.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                DirectoryDialog directoryDialog = new DirectoryDialog(parent.getShell());

                directoryDialog.setText("Browse Template Folder");
                directoryDialog.setMessage("Select template folder:");

                if (selectedTemplate != null)
                {
                    directoryDialog.setFilterPath(selectedTemplate.getPath());
                }

                String newPath = directoryDialog.open();

                if (newPath == null)
                {
                    return;
                }

                int indexOfSelection = templatesStorage.addPath(newPath);

                setComboTemplates();
                combo.select(indexOfSelection);
                setSelectedProject();

                researchTemplatesForPlacehodlers();
            }

        });

        btnProjectFolder.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                FolderSelectionDialog folderSelectionDialog = new FolderSelectionDialog(parent.getShell());

                folderSelectionDialog.addListener(new IClickListener()
                {

                    @Override
                    public void isCLickedOk(String projectPath)
                    {
                        projectFolderPath = projectPath;

                        setProjectPath();
                    }
                });

                folderSelectionDialog.SetSelection(projectFolderPath);

                folderSelectionDialog.open();
            }

        });

        setComboTemplates();
        combo.select(0);

        setSelectedProject();
        setProjectPath();

        researchTemplatesForPlacehodlers();

        setPageComplete(false);
        setControl(this.container);

        initDataBindings();
    }



    private void setSelectedProject()
    {
        IStructuredSelection selection = comboViewer.getStructuredSelection();


        if (selection == null)
        {
            return;
        }

        Template template = (Template) selection.getFirstElement();

        selectedTemplate = template;
    }



    private void researchTemplatesForPlacehodlers()
    {
        placeholderContainer.clear();

        try
        {
            getContainer().run(true, true, new IRunnableWithProgress()
            {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    monitor.beginTask("Search files for placeholders", IProgressMonitor.UNKNOWN);

                    findPlaceholdersInFiles(monitor);
                }
            });
        }
        catch (InvocationTargetException | InterruptedException e1)
        {
            e1.printStackTrace();
        }
    }



    /*
     * Search placeholders in directory
     */
    private void findPlaceholdersInFiles(IProgressMonitor monitor)
    {
        if (selectedTemplate == null)
        {
            return;
        }

        PlaceholderManager placeholderManager = new PlaceholderManager();

        List<String> allPlaceholders = new ArrayList<>();

        FileManager fileManager = new FileManager();

        // Recursive searching.Input - path Output - files
        List<File> files = fileManager.searchFilesInDirectory(new File(selectedTemplate.getPath()), monitor);

        allPlaceholders = placeholderManager.search(files, monitor);


        for (String placeholder : allPlaceholders)
        {
            PlaceholderBean placeholderBean = new PlaceholderBean(placeholder);

            placeholderBean.addChangeValueListener(new IChangeValueListener()
            {
                @Override
                public void isChange()
                {
                    Validate();
                }
            });

            this.placeholderContainer.addPlaceholder(placeholderBean);
        }
    }



    public void copyTemplateAndReplaceyPlaceholders()
    {
        if (selectedTemplate == null)
        {
            return;
        }


        TemplateManager templateManager = new TemplateManager();

        templateManager.copyFilesAndReplacePlaceholders(
                new File(selectedTemplate.getPath()), new File(projectFolderPath), placeholderContainer.getPlaceholders());

        templatesStorage.incrementNumberOfSelection(selectedTemplate);

        ProjectManager.refreshAllProjectInExplorer();
    }



    /*
     * Validate table input
     */
    private boolean Validate()
    {

        for (PlaceholderBean placeholder : this.placeholderContainer.getPlaceholders())
        {
            // Validate - empty field
            if (Validator.filenameIsEmpty(placeholder.getValue()))
            {
                setErrorMessage("Please fill all fields!");

                setPageComplete(false);

                return false;
            }

            // Validate - first letter can't be symbol or number
            if (Validator.filenameStartWithNumberOrSymbol(placeholder.getValue()))
            {
                setErrorMessage("Field can't start with number or symbol!");

                setPageComplete(false);

                return false;
            }

        }

        setPageComplete(true);

        setErrorMessage(null);

        return true;
    }



    /*
     * 
     * Set template path in text box
     * 
     */
    private void setComboTemplates()
    {
        List<Template> templates = templatesStorage.load();

        comboViewer.getCombo().removeAll();

        if (templates != null)
        {
            comboViewer.setInput(templates);
        }
    }



    /*
     * 
     * Set project path in text box
     * 
     */
    private void setProjectPath()
    {
        if (projectFolderPath != null)
        {
            FileManager fileManager = new FileManager();

            String shortProjectPath = fileManager.getParentFolderNameFromFullPath(projectFolderPath);

            txtProjectFolder.setText(shortProjectPath);
        }
    }



    /*
     * Placeholder-value table data binding
     */
    protected DataBindingContext initDataBindings()
    {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        ObservableListContentProvider contentProvider = new ObservableListContentProvider();
        this.viewer.setContentProvider(contentProvider);
        //
        IObservableSet knownElements = contentProvider.getKnownElements();
        //
        final IObservableMap placeholder = BeanProperties.value(PlaceholderBean.class, "placeholder").observeDetail(knownElements);
        //
        final IObservableMap value = BeanProperties.value(PlaceholderBean.class, "value").observeDetail(knownElements);
        //
        IObservableMap[] labelMaps = { placeholder, value };
        ILabelProvider labelProvider = new ObservableMapLabelProvider(labelMaps)
        {

            @Override
            public String getText(Object element)
            {
                // TODO Auto-generated method stub
                return super.getText(element);
            }



            @Override
            public String getColumnText(Object element, int columnIndex)
            {
                if (columnIndex == 0)
                {
                    if (element instanceof PlaceholderBean)
                    {
                        String placeholder = ((PlaceholderBean) element).getPlaceholder();

                        return placeholderContainer.removeCommandFromPlaceholder(placeholder);
                    }

                }

                return null;

            }


        };
        this.viewer.setLabelProvider(labelProvider);
        //
        IObservableList changesTheBeanObserveList = BeanProperties.list("placeholders").observe(this.placeholderContainer);
        //
        this.viewer.setInput(changesTheBeanObserveList);

        return bindingContext;

    }

}
