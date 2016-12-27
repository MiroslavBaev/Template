/**
 * 
 */
package mb.template.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.layout.GridLayout;
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



    public ProjectExplorer(Shell parentShell)
    {
        super(parentShell);
        setShellStyle(SWT.BORDER | SWT.MIN | SWT.RESIZE);

        this.workspacePath = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
        this.selectionPath = null;
    }



    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite container = (Composite) super.createDialogArea(parent);

        container.setLayout(new GridLayout());
        new Label(container, SWT.NONE);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setText("Select export project folder:");

        treeView = new TreeView(container);
        Tree tree = treeView.getTree();
        GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_tree.heightHint = 181;
        gd_tree.widthHint = 404;
        tree.setLayoutData(gd_tree);

        treeView.setSelection(new StructuredSelection(new Object[] {}), true);

        treeView.setInput(new File(workspacePath).listFiles());

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

        if (selectionPath != null)
        {
            File file = new File(selectionPath);

            treeView.setSelection(new StructuredSelection(file));
        }

        return container;
    }



    public String getProjectFolderPath()
    {
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

        removeListeners();

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
