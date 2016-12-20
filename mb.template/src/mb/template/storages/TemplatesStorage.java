/**
 * 
 */
package mb.template.storages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;


/**
 * @author mbaev
 *
 */
public class TemplatesStorage
{
    String settingDirectory;
    String storageFile;



    public TemplatesStorage()
    {
        settingDirectory = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/.settings";
        storageFile = "/templatesInfo.ser";
    }



    public void save(List<Template> templates)
    {
        File settingFile = new File(settingDirectory);

        if (!settingFile.exists())
        {
            settingFile.mkdir();
        }


        try
        {
            FileOutputStream fos = new FileOutputStream(settingDirectory + storageFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(templates.toArray());
            oos.close();
        }
        catch (IOException e)
        {
            // TODO Can't save a template objects in temporary file
            e.printStackTrace();
        }

    }



    public List<Template> load()
    {

        Object[] templates = null;
                
        try
        {
            FileInputStream fis = new FileInputStream(settingDirectory + storageFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            templates = (Object[])ois.readObject();
            ois.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            // TODO Can't load serialization objects
            e.printStackTrace();
        }

        Template[] templateArray = Arrays.copyOf(templates, templates.length, Template[].class);

        List<Template> fixedSizeList = Arrays.asList(templateArray);
        
        List<Template> sorterdList = new ArrayList<>(fixedSizeList);
        
        sorterdList.sort(new Comparator<Template>()
        {

            @Override
            public int compare(Template templateOne, Template templateTwo)
            {
                if(templateOne.getNumberOfSelections() > templateTwo.getNumberOfSelections())
                {
                 return -1;   
                }
                else if(templateOne.getNumberOfSelections() < templateTwo.getNumberOfSelections())
                {
                    return 1;
                }
                
                return 0;
            }
        });
        
        return sorterdList;
        
    }

    
    public void addPath(String path)
    {
        if(path == null)
        {
            return;
        }
        
        List<Template> templates =  this.load();
        
        for (Template template : templates)
        {
            // Element with this path exist. Change it only.
            if( template.getPath().equals(path))
            {
                template.setNumberOfSelections(template.getNumberOfSelections() + 1);
                
                this.save(templates);
                
                return;
            }
        }
        
        // Element with this path is not exist. Create new object and add for first selection.
        // Create and new List, because  
        
        Template newTemplate = new Template(path , 1);
        
        templates.add(newTemplate);
        
        this.save(templates);
    }
    
}
