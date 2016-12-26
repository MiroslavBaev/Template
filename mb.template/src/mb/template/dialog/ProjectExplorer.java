/**
 * 
 */
package mb.template.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import mb.template.listeners.IClickListener;
import mb.template.tree.TreeView;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;


/**
 * @author mbaev
 *
 */
public class ProjectExplorer extends Dialog
{
    private List<IClickListener> listeners = new ArrayList<>();

    private String selectionPath;

    private String workspacePath;
    private TreeView treeView;



    /**
     * @param parentShell
     */
    public ProjectExplorer(Shell parentShell)
    {
        super(parentShell);

        this.workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
        this.selectionPath = null;
    }



    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite container = (Composite) super.createDialogArea(parent);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setText("Choose a export project folder:");

        treeView = new TreeView(container);
        Tree tree = treeView.getTree();
        GridData gd_tree = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_tree.heightHint = 181;
        gd_tree.widthHint = 404;
        tree.setLayoutData(gd_tree);

        treeView.setSelection(new StructuredSelection(new Object[] {}), true);

        treeView.setInput(new File(workspacePath).listFiles());

        if (selectionPath != null)
        {
            File file = new File(selectionPath);

            treeView.setSelection(new StructuredSelection(file));
        }

        tree.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                TreeItem[] selection = tree.getSelection();

                if (selection.length < 0)
                    return;

                selectionPath = selection[0].getData().toString();

            }

        });

        return container;
    }



    public String getProjectName()
    {
        workspacePath = workspacePath.replace("/", "\\");
        
        return selectionPath.replace(workspacePath, "");
    }



    public String getProjectFolderFullPath()
    {
        return selectionPath;
    }



    public void setFilterPath(String path)
    {
        if (path == null)
        {
            return;
        }

        selectionPath = path;
    }



    @Override
    protected void okPressed()
    {
        fireButtonOkClick();

        super.okPressed();
    }



    private void fireButtonOkClick()
    {
        if (listeners.isEmpty())
        {
            return;
        }

        for (IClickListener listener : listeners)
        {
            listener.isCLickedOk();
        }

    }



    public void addListener(IClickListener listener)
    {
        this.listeners.add(listener);
    }



    public void removeListeners()
    {
        this.listeners.clear();
    }



    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);

        newShell.setText("Folder Selection");
    }


}
