package mb.template.tree.sorter;

import java.io.File;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class FileComparator extends ViewerComparator
{

    /**
     *  Sorts the elements in tree 
     * 
     * @author mbaev
     *
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {

        if (((File) e1).isDirectory() && ((File) e2).isFile())
        {
            return -1;
        }
        else if (((File) e2).isDirectory() && ((File) e1).isFile())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

}
