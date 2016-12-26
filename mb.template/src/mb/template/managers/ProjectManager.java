package mb.template.managers;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
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
    private static IResource selectedProjectFolderResource;


//    public static String getProjectExplorerProject(String name)
//    {
//        IWorkspace workspace = ResourcesPlugin.getWorkspace();
//
//        IProject project = workspace.getRoot().getProject(name);
//
//        return project.getLocation().toString();
//    }



    public static String getSelectedProjectFolder()
    {
        if(selectedProjectFolderResource == null)
        {
            return null;
            
        }
        return selectedProjectFolderResource.getFullPath().toString();
    }



    /*
     * Get selected project from package explorer
     */
    public static String getSelectedProjectFromPackageExplorerFullPath()
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
            IResource resource = (IResource) element;

            // return only Test/xxxxx
            // return resource.getFullPath().toString();

            // RETURN PROJECT

            selectedProjectFolderResource = resource;

            return resource.getLocation().toString();
            
        }
        else if (element instanceof IAdaptable)
        {
            IAdaptable adaptable = (IAdaptable) element;
            IResource resource = adaptable.getAdapter(IResource.class);

            selectedProjectFolderResource = resource;

            return resource.getLocation().toString();
        }

        return null;
    }



    public static void refreshFolder(String path)
    {
        
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        
        IProject project = workspace.getRoot().getProject(path);
        
        
        try
        {
            project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        }
        catch (CoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
//        IFolder folder = selectedProjectFolderResource.getProject()getFolder(new Path(path));
//
//        try
//        {
//            folder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
//        }
//        catch (CoreException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

}
