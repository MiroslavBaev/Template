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
    String settingDirectoryPath;
    String storageFilePath;
    String fullPath;



    public TemplatesStorage()
    {
        settingDirectoryPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/.settings";
        storageFilePath = "/templatesInfo.ser";
        this.fullPath = settingDirectoryPath + storageFilePath;
    }



    public void save(List<Template> templates)
    {
        File settingFile = new File(settingDirectoryPath);

        if (!settingFile.exists())
        {
            settingFile.mkdir();
        }


        try
        {
            FileOutputStream fos = new FileOutputStream(fullPath);
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
        if (!new File(fullPath).exists())
        {
            return null;
        }

        Object[] templates = null;

        try
        {
            FileInputStream fis = new FileInputStream(settingDirectoryPath + storageFilePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            templates = (Object[]) ois.readObject();
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
                if (templateOne.getNumberOfSelections() > templateTwo.getNumberOfSelections())
                {
                    return -1;
                }
                else if (templateOne.getNumberOfSelections() < templateTwo.getNumberOfSelections())
                {
                    return 1;
                }

                return 0;
            }
        });

        return sorterdList;

    }



    public void removePath(int index)
    {
        List<Template> templates = this.load();
        if(templates == null)
        {
            return;
        }

        templates.remove(index);

        this.save(templates);
    }



    /*
     * 
     * Create template object from path or increment number of selections, if such object exist.
     * Method return position index of object in List
     */
    public int addPath(String path)
    {
        List<Template> templates = this.load();
        
        if (path == null || templates == null)
        {
            return -1;
        }

        for (int i = 0; i < templates.size(); i++)
        {
            // Element with this path exist. Change it only.
            Template currentTemplate = templates.get(i);
            if (currentTemplate.getPath().equals(path))
            {
                templates.get(i).setNumberOfSelections(templates.get(i).getNumberOfSelections() + 1);

                this.save(templates);

                // Must be sorted again in order to take the correct index for combo selection.
                templates = this.load();

                return templates.indexOf(currentTemplate);
            }
        }

        // Element with this path is not exist. Create new object and add 1 for first selection.
        Template newTemplate = new Template(path, 1);

        templates.add(newTemplate);

        this.save(templates);

        return (this.load().size() - 1);
    }

}
