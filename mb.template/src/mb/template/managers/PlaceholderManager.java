/**
 * 
 */
package mb.template.managers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import mb.template.placeholder.PlaceholderPattern;


/**
 * Placeholder finder. Search placeholders in given directory /in file name and file content/
 * 
 * @author mbaev
 *
 */
public class PlaceholderManager
{


    public PlaceholderManager()
    {
    }



    public ArrayList<String> searchPlaceholders(List<File> files, IProgressMonitor monitor)
    {
        ArrayList<String> allFoundPlaceholders = new ArrayList<>();

        SearchManager searchManager = new SearchManager(new PlaceholderPattern().getPatterns());
        
        List<String> foundedMatches = null;

        Charset charset = StandardCharsets.UTF_8;

        for (int i = 0; i < files.size(); i++)
        {
            if (monitor.isCanceled())
            {
                return new ArrayList<>();
            }

            // Search in filename
            String fileNameWithoutExtension = searchManager.removeExtensionFromFile(files.get(i).getName());

            foundedMatches = searchManager.search(fileNameWithoutExtension);

            for (String foundMatch : foundedMatches)
            {
                if (!allFoundPlaceholders.contains(foundMatch))
                {
                    System.out.println(foundMatch);
                    allFoundPlaceholders.add(foundMatch);
                }
            }

            // Search file in content
            if (files.get(i).isFile())
            {
                Path path = files.get(i).toPath();

                try
                {
                    String content = new String(Files.readAllBytes(path), charset);

                    foundedMatches = searchManager.search(content);

                }
                catch (IOException e)
                {
                    MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Error", "There was a problem with reading the template files");

                    e.printStackTrace();
                }

                for (String foundMatch : foundedMatches)
                {

                    if (!allFoundPlaceholders.contains(foundMatch))
                    {
                        allFoundPlaceholders.add(foundMatch);
                    }
                }

            }
        }

        return allFoundPlaceholders;
    }

}
