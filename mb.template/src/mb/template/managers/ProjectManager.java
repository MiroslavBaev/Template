package mb.template.managers;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * Project Manager.
 * 
 * @author mbaev
 *
 */
public class ProjectManager
{
    /*
     * Return short path from given IResource
     */
    public static String getParentPath(IResource resource)
    {
        if (resource == null)
            return null;

        String parentPath = resource.getParent().toString();

        int slashIndex = parentPath.indexOf("/");

        if (slashIndex != -1)
        {
            parentPath = parentPath.substring(slashIndex + 1);
        }

        return new Path(parentPath).toOSString();


    }



    /*
     * Return full path from given IResource
     */
    public static String getLocationPath(IResource resource)
    {
        if (resource == null)
            return null;

        return resource.getLocation().toOSString();
    }



    /*
     * Return selected project resource from Package Explorer
     */
    public static IResource getSelectedResourceFromPackageExplorer()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        ISelection selection = window
                .getSelectionService()
                .getSelection("org.eclipse.jdt.ui.PackageExplorer");

        if (selection == null)
        {
            return null;
        }

        IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;

        Object element = iStructuredSelection.getFirstElement();

        if (element instanceof IResource)
        {
            return (IResource) element;

        }
        else if (element instanceof IAdaptable)
        {
            IAdaptable adaptable = (IAdaptable) element;

            return adaptable.getAdapter(IResource.class);
        }

        return null;
    }



    /*
     * Refresh given IResource
     */
    public static void refreshFolder(IResource resource)
    {
        if (resource != null)
        {
            try
            {
                resource.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            }
            catch (CoreException e)
            {
                e.printStackTrace();
            }
        }

    }

}
