package mb.template.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
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
     * Get workspace path 
     * 
     */
    public String getWorkspacePath()
    {
        return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/";
    }
    
    
    /*
     * 
     * Remove directory to workspace folder. Input full path to project, output only name of project.
     *
     * Example:
     * C:\Users\MBaev.HILSCHERDTC\Desktop\runtime-EclipseApplication\Test - > Test
     * 
     */
    public String getShortProjectPath(String projectFolderPath)
    {
        if(projectFolderPath == null)
        {
            return null;
        }
        
        String workspacePath = getWorkspacePath();
        
        if (workspacePath.contains("/"))
        {
            workspacePath = workspacePath.replace("/", "\\");
        }

        if (projectFolderPath.contains("/"))
        {
            projectFolderPath = projectFolderPath.replace("/", "\\");
        }

        projectFolderPath = removeFileFromDirectoryPath(projectFolderPath);

        return projectFolderPath.replace(workspacePath, "");
    }
    
    /*
    *
    * Remove file from directory path if exist
    * 
    * Example:
    * C:/ProgramFiles/Work/file.exe - > C:/ProgramFiles/Work
    * 
    */
   public String removeFileFromDirectoryPath(String fullPath)
   {
       if (fullPath == null)
       {
           return null;
       }
       if (new File(fullPath).isFile())
       {
           char separator = '\\';

           int lastSeparatorIndex = fullPath.lastIndexOf(separator);

           return fullPath.substring(0, lastSeparatorIndex);
       }
       else
       {
           return fullPath;
       }
   }
    
    
    /*
     * 
     * Refresh selected project
     * 
     */
    public static void refreshCurrentProjectInExplorer()
    {
        IProject project = getCurrentSelectedProject();
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
    
    public ArrayList<IProject> getAllProjects()
    {
     
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IProject[] projects = workspaceRoot.getProjects();
        
        return new ArrayList<IProject>(Arrays.asList(projects));
    }


    /*
     * 
     * Get selected project
     * 
     */
    public static IProject getCurrentSelectedProject()
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
        ISelectionService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();

        IStructuredSelection selection = (IStructuredSelection) service.getSelection();

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
