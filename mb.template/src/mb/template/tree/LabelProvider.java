/**
 * 
 */
package mb.template.tree;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


/**
 * Label provider for TreeViewer
 * 
 * @author mbaev
 *
 */
public class LabelProvider implements ILabelProvider
{
    private List<ILabelProviderListener> listeners;

    private Image fileImage;
    private Image projectImage;
    private Image folderImage;

    boolean preserveCase;



    public LabelProvider()
    {
        this.listeners = new ArrayList<ILabelProviderListener>();


//        fileImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
//        
//        folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
//        
//        otherImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);


        ResourceManager resManager = new LocalResourceManager(JFaceResources.getResources());
        Bundle bundle = FrameworkUtil.getBundle(this.getClass());

        // file image
        URL url = FileLocator.find(bundle, new Path("icons/file.png"), null);
        ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
        fileImage = resManager.createImage(imageDescriptor);

        // project image
        url = FileLocator.find(bundle, new Path("icons/project.png"), null);
        imageDescriptor = ImageDescriptor.createFromURL(url);
        projectImage = resManager.createImage(imageDescriptor);

        // folder image
        url = FileLocator.find(bundle, new Path("icons/folder.png"), null);
        imageDescriptor = ImageDescriptor.createFromURL(url);
        folderImage = resManager.createImage(imageDescriptor);

        setPreserveCase(true);
    }



    private void setPreserveCase(boolean preserveCase)
    {
        this.preserveCase = preserveCase;

        LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
        for (int i = 0, n = listeners.size(); i < n; i++)
        {
            ILabelProviderListener ilpl = (ILabelProviderListener) listeners
                    .get(i);
            ilpl.labelProviderChanged(event);
        }
    }



    public Image getImage(Object arg0)
    {
        File file = ((File) arg0);


        if (isProject(file))
        {
            return projectImage;
        }
        else if (file.isFile())
        {
            return fileImage;
        }
        else if (file.isDirectory())
        {
            return folderImage;
        }
        else
        {
            return null;
        }
    }



    private boolean isProject(File file)
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        IProject[] projects = workspace.getRoot().getProjects();
        
        for (IProject project : projects)
        {
            if(project.getName().equals(file.getName()))
            {
                return true;
            }
        }

        return false;
    }



    public String getText(Object arg0)
    {
        String text = ((File) arg0).getName();

        if (text.length() == 0)
        {
            text = ((File) arg0).getPath();
        }

        return preserveCase ? text : text.toUpperCase();
    }



    public void addListener(ILabelProviderListener arg0)
    {
        listeners.add(arg0);
    }



    public void dispose()
    {
        // Dispose the images
//        if (dir != null)
//            dir.dispose();
//        if (file != null)
//            file.dispose();
    }



    public boolean isLabelProperty(Object arg0, String arg1)
    {
        return false;
    }



    public void removeListener(ILabelProviderListener arg0)
    {
        listeners.remove(arg0);
    }
}
