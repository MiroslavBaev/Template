/**
 * 
 */
package mb.template.tree;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import mb.template.tree.filter.ProjectsFilter;
import mb.template.tree.sorter.FileComparator;

/**
 * TreeViewer
 * 
 * @author mbaev
 *
 */
public class TreeView extends TreeViewer
{

    
    @Override
    public void setSelection(ISelection selection, boolean reveal)
    {
        // TODO Auto-generated method stub
        super.setSelection(selection, reveal);
    }

    public TreeView(Composite parent)
    {
        super(parent,SWT.BORDER);
        setContentProvider(new ContentProvider());
        setLabelProvider(new LabelProvider());
        setFilters(new ViewerFilter[] { new ProjectsFilter()});
        setComparator(new FileComparator());
    }

}
