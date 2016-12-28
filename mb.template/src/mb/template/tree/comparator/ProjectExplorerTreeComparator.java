/**
 * 
 */
package mb.template.tree.comparator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;


/**
 * @author mbaev
 *
 */
public class ProjectExplorerTreeComparator extends ViewerComparator
{

    @Override
    public int compare(Viewer viewer, Object elementOne, Object elementTwo)
    {
        if (elementOne instanceof IProject && elementTwo instanceof IProject)
        {
            return 0;
        }
        else if (elementOne instanceof IProject && elementTwo instanceof IFile)
        {
            return 1;
        }
        else if (elementOne instanceof IProject && elementTwo instanceof IFolder)
        {
            return 1;
        }
        else if (elementOne instanceof IProject && elementTwo instanceof IFile)
        {
            return -1;
        }
        else if (elementOne instanceof IProject && elementTwo instanceof IFolder)
        {
            return -1;
        }
        else if (elementOne instanceof IFolder && elementTwo instanceof IFile)
        {
            return -1;
        }

        else if (elementTwo instanceof IFolder && elementOne instanceof IFile)
        {
            return 1;
        }
       
        else
        {
            return 0;
        }

    }

}

