package mb.template.dialog;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import mb.template.managers.ProjectManager;
import mb.template.tree.comparator.ProjectExplorerTreeComparator;
import mb.template.tree.filter.ProjectExplorerTreeFilter;


/**
 * Project Explorer
 * 
 * @author mbaev
 *
 */
public class ProjectExplorerDialog extends ElementTreeSelectionDialog
{
    public ProjectExplorerDialog(Shell parent, IBaseLabelProvider labelProvider, ITreeContentProvider contentProvider)
    {
        super(parent, labelProvider, contentProvider);

        setTitle("Browse Project Folder");
        setMessage("Select output project folder:");

        addFilter(new ProjectExplorerTreeFilter());
        setComparator(new ProjectExplorerTreeComparator());
    }



    @Override
    public Object getFirstResult()
    {
        return ProjectManager.getParentResource((IResource) super.getFirstResult());
    }

}
