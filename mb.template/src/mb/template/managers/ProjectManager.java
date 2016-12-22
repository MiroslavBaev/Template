package mb.template.managers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
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
     * 
     * Refresh selected project
     * s
     */
    public static void refreshProjectInExplorer()
    {
        IProject project = getSelectedProject();
        
        try
        {
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }


    /*
     * 
     * Refresh all projects
     * 
     */
    public static void refreshAllProjectInExplorer()
    {
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IProject[] projects = workspaceRoot.getProjects();
        for (int i = 0; i < projects.length; i++)
        {
            IProject project = projects[i];
            
            if (project.isOpen())
            {
                try
                {
                    project.refreshLocal(IResource.DEPTH_INFINITE, null);
                }
                catch (CoreException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
//    public ArrayList<IProject> getAllProjects()
//    {
//     
//        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
//        IProject[] projects = workspaceRoot.getProjects();
//        
//        return new ArrayList<IProject>(Arrays.asList(projects));
//    }


    /*
     * 
     * Get selected project
     * 
     */
    public static IProject getSelectedProject()
    {
        IProject project = null;
        ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();

        ISelection selection = selectionService.getSelection();

        
        if (selection instanceof IStructuredSelection)
        {
            Object element = ((IStructuredSelection) selection).getFirstElement();

            if (element instanceof IResource)
            {
                project = ((IResource) element).getProject();
            }
            else if(element != null)
            {
                IAdaptable adaptable = (IAdaptable) element;
                IResource adapter = adaptable.getAdapter(IResource.class);
                project = adapter.getProject();
            }
        }
        return project;
    }


    /*
     * 
     * Refresh selected project
     * 
     */
    public static String getSelectedElementPath()
    {
        IWorkbenchWindow window =
                PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            ISelection selection = window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");

        if(selection == null)
        {
            return null;
        }
        
        IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
        Object element = iStructuredSelection.getFirstElement();

        
        if (element instanceof IResource)
        {
            IResource resursable = (IResource) element;

            return resursable.getLocation().toString();
        }
        else if (element instanceof IAdaptable)
        {
            IAdaptable adaptable = (IAdaptable) element;
            IResource adapter = adaptable.getAdapter(IResource.class);

            return adapter.getLocation().toString();
        }

        return null;
    }
}
