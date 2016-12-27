package mb.template.managers;

import java.io.File;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
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
    private static String selectedProjectFolderFullPath;
    private static String selectedProjectFolderPath;



    /*
     * Return selected project folder path from Package Explorer
     */
    public static String getSelectedProjectFolder()
    {
        if (selectedProjectFolderFullPath == null)
        {
            return null;
        }

        return selectedProjectFolderPath;
    }



    /*
     * Return selected project folder full path from Package Explorer
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

            selectedProjectFolderFullPath = resource.getLocation().toOSString();
            selectedProjectFolderPath = resource.getFullPath().toOSString();

            return selectedProjectFolderFullPath;

        }
        else if (element instanceof IAdaptable)
        {
            IAdaptable adaptable = (IAdaptable) element;

            IResource resource = adaptable.getAdapter(IResource.class);

            selectedProjectFolderFullPath = resource.getLocation().toOSString();
            selectedProjectFolderPath = resource.getFullPath().toOSString();

            return selectedProjectFolderFullPath;
        }

        return null;
    }



    public static void refreshFolder(String path)
    {
        if (path == null)
        {
            return;
        }

        if (path.startsWith("\\"))
        {
            path = path.replaceFirst(Pattern.quote(File.separator), "");
        }

        String projectName = null;
        String folderPath = null;

        int projectNameStartIndex = 0;
        int projectNameEndIndex = path.indexOf("\\");

        if (projectNameEndIndex == -1)
        {
            projectName = path.substring(projectNameStartIndex);
        }
        else
        {
            projectName = path.substring(0, projectNameEndIndex);

            folderPath = path.substring(projectNameEndIndex);
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        IProject project = workspace.getRoot().getProject(projectName);

        try
        {
            if (folderPath != null)
            {
                IFolder folder = project.getFolder(folderPath);

                folder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            }
            else
            {
                project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            }

        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
      
    }

}
