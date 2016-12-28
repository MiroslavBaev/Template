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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import mb.template.dialog.ProjectExplorerDialog;
import mb.template.help.TemplateHelpContextIds;
import mb.template.listeners.IChangeValueListener;
import mb.template.managers.FileManager;
import mb.template.managers.PlaceholderManager;
import mb.template.managers.ProjectManager;
import mb.template.placeholder.Placeholder;
import mb.template.placeholder.PlaceholderContainer;
import mb.template.storages.TemplateFolder;
import mb.template.storages.TemplateFolderStorage;
import mb.template.validator.Validator;
import mb.template.wizard.table.editor.ColumnEditingSupport;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;


/**
 * Wizard page. Select a directory where the template to be copied
 * 
 * @author mbaev
 */
public class TemplatePage extends WizardPage
{
    private Composite container;
    private TableViewer viewer;

    private Text txtProjectPath;
    private ComboViewer comboTemplatePaths;

    private PlaceholderContainer placeholderContainer;

    private String selectedProjectItemLocationPath;
    private String selectedProjectItemFullPath;

    private IResource resource;

    private TemplateFolderStorage templatesStorage;
    private PlaceholderManager placeholderManager;

    private TemplateFolder selectedTemplate;
    private boolean projectFolderIsSelected;



    public TemplatePage()
    {
        super("Select project");
        setTitle("Template");
        setDescription("Create a new template");

        this.placeholderContainer = new PlaceholderContainer();

        this.selectedProjectItemLocationPath = null;
        this.selectedProjectItemFullPath = null;

        this.resource = null;

        this.templatesStorage = new TemplateFolderStorage();
        this.placeholderManager = new PlaceholderManager();

        this.selectedTemplate = null;
        this.projectFolderIsSelected = false;


    }



    public void createControl(Composite parent)
    {
        this.container = new Composite(parent, SWT.NONE);
        this.container.setLayout(new GridLayout(3, false));
        //
        setControl(this.container);
        //
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, TemplateHelpContextIds.TEMPLATE_WIZARD);
        //
        Label lblTemplateSourceFolder = new Label(container, SWT.NONE);
        lblTemplateSourceFolder.setText(" Source template folder");
        //
        comboTemplatePaths = new ComboViewer(container, SWT.READ_ONLY | SWT.V_SCROLL);
        Combo combo = comboTemplatePaths.getCombo();
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

                    addComboTemplatePaths();

                    placeholderContainer.clear();
                }
            }

        });
        combo.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                getSelectedTemplateFromCombo();

                searchTemplateForPlacehodlers();
            }

        });
        //
        comboTemplatePaths.setContentProvider(ArrayContentProvider.getInstance());
        comboTemplatePaths.setLabelProvider(new LabelProvider()
        {
            @Override
            public String getText(Object element)
            {
                if (element instanceof TemplateFolder)
                {
                    TemplateFolder template = (TemplateFolder) element;
                    return template.getPath();
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
        lblProjectFolder.setText("Export project folder");
        //
        txtProjectPath = new Text(container, SWT.BORDER);
        txtProjectPath.setEditable(false);
        txtProjectPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtProjectPath.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent e)
            {
                setErrorMessage(null);

                projectFolderIsSelected = true;
            }
        });
        //
        Button btnProjectFolder = new Button(container, SWT.NONE);
        btnProjectFolder.setText("Browse..");
        //
        lblSeparator = new Label(container, SWT.NONE | SWT.SEPARATOR | SWT.HORIZONTAL);
        lblSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
        //
        this.viewer = new TableViewer(this.container, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        //
        Table table = this.viewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        // Create column
        TableViewerColumn column = new TableViewerColumn(this.viewer, SWT.NONE);
        column.getColumn().setWidth(271);
        column.getColumn().setText("Placeholder");
        // Create column
        column = new TableViewerColumn(this.viewer, SWT.NONE);
        column.setEditingSupport(new ColumnEditingSupport(this.viewer));
        column.getColumn().setWidth(287);
        column.getColumn().setText("Value");
        //
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

                addComboTemplatePaths();
                combo.select(indexOfSelection);
                getSelectedTemplateFromCombo();

                searchTemplateForPlacehodlers();
            }

        });

        btnProjectFolder.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ProjectExplorerDialog projectExplorerDialog = new ProjectExplorerDialog(parent.getShell(), new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());

                projectExplorerDialog.setInput(ResourcesPlugin.getWorkspace().getRoot());

                if (resource != null)
                {
                    projectExplorerDialog.setInitialSelection(resource);
                }

                if (projectExplorerDialog.open() == Window.OK)
                {
                    resource = (IResource) projectExplorerDialog.getFirstResult();

                    selectedProjectItemLocationPath = ProjectManager.getLocationPath(resource);
                    selectedProjectItemFullPath = ProjectManager.getFullPath(resource);

                    addTextProjectPath(selectedProjectItemFullPath);
                }
            }

        });

        addComboTemplatePaths();
        combo.select(0);

        getSelectedTemplateFromCombo();

        this.resource = ProjectManager.getSelectedResourceFromPackageExplorer();

        this.selectedProjectItemLocationPath = ProjectManager.getLocationPath(this.resource);
        this.selectedProjectItemFullPath = ProjectManager.getFullPath(this.resource);

        addTextProjectPath(this.selectedProjectItemFullPath);

        searchTemplateForPlacehodlers();

        initDataBindings();
    }



    private void getSelectedTemplateFromCombo()
    {
        IStructuredSelection selection = comboTemplatePaths.getStructuredSelection();

        if (selection == null)
        {
            return;
        }

        TemplateFolder template = (TemplateFolder) selection.getFirstElement();

        this.selectedTemplate = template;
    }



    /*
     * Add template paths in combo box
     */
    private void addComboTemplatePaths()
    {
        List<TemplateFolder> templates = templatesStorage.loadPaths();

        comboTemplatePaths.getCombo().removeAll();

        if (templates != null)
        {
            comboTemplatePaths.setInput(templates);
        }
    }



    /*
     * Add project path in combo box
     */
    private void addTextProjectPath(String fullPath)
    {
        if (fullPath == null)
        {
            return;
        }

        txtProjectPath.setText(fullPath);
    }



    private void searchTemplateForPlacehodlers()
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
        if (this.selectedTemplate == null)
        {
            return;
        }

        List<String> allPlaceholders = new ArrayList<>();

        FileManager fileManager = new FileManager();

        // Recursive searching.Input - path Output - files
        List<File> files = fileManager.searchFilesInDirectory(new File(this.selectedTemplate.getPath()), monitor);

        allPlaceholders = this.placeholderManager.searchPlaceholdersInFiles(files, monitor);

        for (String placeholder : allPlaceholders)
        {
            Placeholder placeholderBean = new Placeholder(placeholder);

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



    /*
     * Read templates from directory, replace placeholders and copy to selected project directory.
     * This method called when Finish button is clicked.
     */
    public void copyTemplateAndReplaceyPlaceholders()
    {
        if (this.selectedTemplate == null)
        {
            return;
        }

        this.placeholderManager.copyFilesAndReplacePlaceholders(
                new File(this.selectedTemplate.getPath()), new File(this.selectedProjectItemLocationPath), this.placeholderContainer.getPlaceholders());

        this.templatesStorage.incrementNumberOfSelection(this.selectedTemplate);

        ProjectManager.refreshFolder(this.resource);
    }



    /*
     * Validate table input
     */
    private boolean Validate()
    {

        for (Placeholder placeholder : this.placeholderContainer.getPlaceholders())
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

        if (projectFolderIsSelected)
        {
            setPageComplete(true);

            setErrorMessage(null);
        }
        else
        {
            setErrorMessage("Project foldert should be selected!");
        }

        return true;
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
        final IObservableMap placeholder = BeanProperties.value(Placeholder.class, "placeholder").observeDetail(knownElements);
        //
        final IObservableMap value = BeanProperties.value(Placeholder.class, "value").observeDetail(knownElements);
        //
        IObservableMap[] labelMaps = { placeholder, value };
        ILabelProvider labelProvider = new ObservableMapLabelProvider(labelMaps)
        {
            @Override
            public String getColumnText(Object element, int columnIndex)
            {
                if (columnIndex == 0)
                {
                    return ((Placeholder) element).getPlaceholderWithoutCommand();
                }
                else
                {
                    return super.getColumnText(element, columnIndex);
                }

            }


        };
        this.viewer.setLabelProvider(labelProvider);

        IObservableList changesTheBeanObserveList = BeanProperties.list("placeholders").observe(this.placeholderContainer);

        this.viewer.setInput(changesTheBeanObserveList);

        setPageComplete(false);

        return bindingContext;
    }

}
