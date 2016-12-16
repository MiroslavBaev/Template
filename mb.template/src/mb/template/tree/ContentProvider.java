/**
 * 
 */
package mb.template.tree;

import java.io.File;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 * Content provider for TreeViewer
 * 
 * @author mbaev
 *
 */
public class ContentProvider extends ArrayContentProvider  implements ITreeContentProvider
{
    

    @Override
    public Object[] getChildren(Object parentElement)
    {
        File file = (File) parentElement;
        return file.listFiles(new FolderFilter());
    }



    @Override
    public Object getParent(Object element)
    {
        File file = (File) element;
        return file.getParentFile();
    }



    @Override
    public boolean hasChildren(Object element)
    {
        File file = (File) element;
        if (file.isDirectory())
        {
            return true;
        }
        return false;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }
}