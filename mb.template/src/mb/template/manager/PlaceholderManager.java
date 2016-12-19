/**
 * 
 */
package mb.template.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;


/**
 * Placeholder finder. Search placeholders in given directory /in file name and file content/
 * 
 * @author mbaev
 *
 */
public class PlaceholderManager
{
    //private final static String PLACEHOLDER_REGEX = "\\$\\{(.*?)\\}";
    private Pattern pattern;
    private Matcher matcher;



    public PlaceholderManager()
    {
        this.pattern = null;
        this.matcher = null;
    }



    public ArrayList<String> search(List<File> files, IProgressMonitor monitor)
    {
        ArrayList<String> allFoundPlaceholders = new ArrayList<>();

        this.pattern = Pattern.compile("(?i)__Prefix____TaskName__");
        this.matcher = null;
        Charset charset = StandardCharsets.UTF_8;

        for (int i = 0; i < files.size(); i++)
        {
            if (monitor.isCanceled())
            {
                return new ArrayList<>();
            }
            
            if (files.get(i).isDirectory()) // Search in filename for placeholders
            {
                String fileName = files.get(i).getName();
                this.matcher = pattern.matcher(fileName);

                while (this.matcher.find())
                {
                    System.out.println(this.matcher.group().toString());
                    if (!allFoundPlaceholders.contains(this.matcher.group()))
                    {
                        System.out.println(this.matcher.group());
                        allFoundPlaceholders.add(this.matcher.group());
                    }
                }
            }
            else if (files.get(i).isFile()) // Search in file for placeholders
            {
                Path path = files.get(i).toPath();

                String content = null;

                try
                {
                    content = new String(Files.readAllBytes(path), charset);
                    this.matcher = pattern.matcher(content);

                }
                catch (IOException e)
                {
                    MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Error", "There was a problem with reading the template files");

                    e.printStackTrace();
                }

                while (this.matcher.find())
                {
                    System.out.println(this.matcher.group().toString());
                    
                    if (!allFoundPlaceholders.contains(this.matcher.group()))
                    {
                        allFoundPlaceholders.add(this.matcher.group());
                    }
                }

            }

        }

        return allFoundPlaceholders;
    }

}
