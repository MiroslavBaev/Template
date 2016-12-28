package mb.template.managers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
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
    public static String getFullPath(IResource resource)
    {
        if (resource == null)
            return null;

        String fullPath = resource.getFullPath().toOSString();

        int slashIndex = fullPath.indexOf("\\");

        if (slashIndex != -1)
        {
            fullPath = fullPath.substring(slashIndex + 1);
        }

        return fullPath;


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
            return getParentResource((IResource) element);

        }
        else if (element instanceof IAdaptable)
        {
            IAdaptable adaptable = (IAdaptable) element;
            
            IResource resource = adaptable.getAdapter(IResource.class);
            
            return getParentResource(resource);
        }

        return null;
    }



    /*
     * Refresh given IResource
     */
    public static void refreshFolder(IResource resource)
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



    /*
     * Get parent from given IReseource, if instance of IFile. Otherwise return same IResource
     */
    public static IResource getParentResource(IResource resource)
    {
        if (resource instanceof IFile)
        {
            return resource.getParent();
        }
        else
        {
            return resource;
        }

    }
}
