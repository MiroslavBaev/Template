/**
 * 
 */
package mb.template.tree.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;


/**
 * Filter used to filter directory in Project Explorer Dialog
 * 
 * @author mbaev
 *
 */
public class ProjectExplorerTreeFilter extends ViewerFilter
{
    private static final String DOT_METADATA = ".metadata";
    private static final String DOT_SETTINGS = ".settings";
    private static final String DOT_SVN = ".svn";
    private static final String DOT_PROJECT = ".project";
    private static final String DOT_CLASSPATH = ".classpath";



    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        if (element.toString().contains(DOT_METADATA) ||
                element.toString().contains(DOT_SETTINGS) ||
                element.toString().contains(DOT_SVN) ||
                element.toString().contains(DOT_PROJECT) ||
                element.toString().contains(DOT_CLASSPATH))
        {
            return false;
        }

        return true;
    }

}