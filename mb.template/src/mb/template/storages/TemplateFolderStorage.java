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
public class TemplateFolderStorage
{
    String settingDirectoryPath;
    String storageFile;
    String fullPath;



    public TemplateFolderStorage()
    {
        this.settingDirectoryPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/.settings";
        this.storageFile = "/templates.ser";
        this.fullPath = this.settingDirectoryPath + this.storageFile;
    }



    public void savePaths(List<TemplateFolder> templates)
    {
        createDirectoryAndStorageFileIfNotExist();

        try
        {
            FileOutputStream fos = new FileOutputStream(this.fullPath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(templates.toArray());
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }



    public List<TemplateFolder> loadPaths()
    {
        createDirectoryAndStorageFileIfNotExist();

        File storageFile = new File(this.fullPath);

        if (storageFile.length() <= 0)
        {
            return new ArrayList<>();
        }

        Object[] templates = null;

        try
        {
            FileInputStream fis = new FileInputStream(this.fullPath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            templates = (Object[]) ois.readObject();
            ois.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        TemplateFolder[] templateArray = Arrays.copyOf(templates, templates.length, TemplateFolder[].class);

        List<TemplateFolder> fixedSizeList = Arrays.asList(templateArray);

        List<TemplateFolder> sorterdList = new ArrayList<>(fixedSizeList);

        sorterdList.sort(new Comparator<TemplateFolder>()
        {

            @Override
            public int compare(TemplateFolder templateOne, TemplateFolder templateTwo)
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
        List<TemplateFolder> templates = this.loadPaths();
        if (templates == null)
        {
            return;
        }

        templates.remove(index);

        this.savePaths(templates);
    }



    /*
     * Create template object from path, add it to template list and return its index. If object with this path exist, only return index.
     * Method return position index of object in List. This index used with Combo box.
     */
    public int addPath(String path)
    {
        List<TemplateFolder> templates = this.loadPaths();

        if (path == null)
        {
            return -1;
        }

        if (templates != null)
        {
            for (int i = 0; i < templates.size(); i++)
            {
                TemplateFolder currentTemplate = templates.get(i);

                // Element with this path exist. Only return its index.
                if (currentTemplate.getPath().equals(path))
                {
                    return templates.indexOf(currentTemplate);
                }
            }
        }
        // Element with this path is not exist. Create new object and add 1 for first selection.
        TemplateFolder newTemplate = new TemplateFolder(path, 1);

        templates.add(newTemplate);

        this.savePaths(templates);

        return (this.loadPaths().size() - 1);
    }


    public void incrementNumberOfSelection(TemplateFolder template)
    {
        if(template == null)
        {
            return;
        }
        
        List<TemplateFolder> templates = this.loadPaths();
        
        TemplateFolder currentTemplate = templates.get(templates.indexOf(template));

        currentTemplate.setNumberOfSelections(currentTemplate.getNumberOfSelections() + 1);

        this.savePaths(templates);
    }
    

    private void createDirectoryAndStorageFileIfNotExist()
    {
        // Create .setting folder if not exist
        File settingDirectory = new File(settingDirectoryPath);

        if (!settingDirectory.exists())
        {
            settingDirectory.mkdir();
        }


        // Create storage file
        File storageFile = new File(this.fullPath);

        if (!storageFile.exists())
        {
            try
            {
                storageFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Can't create storage file
                e.printStackTrace();
            }
        }

    }

}
