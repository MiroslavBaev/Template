/**
 * 
 */
package mb.template.tree.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;


/**
 * Filter used to filter directory in Tree View
 * 
 * @author mbaev
 *
 */
public class ProjectsFilter extends ViewerFilter
{

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        if (element.toString().contains(".metadata") || element.toString().contains(".settings"))
        {
            return false;
        }

        return true;
    }

}
