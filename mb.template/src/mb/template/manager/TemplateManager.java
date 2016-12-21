package mb.template.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import mb.template.placeholder.PlaceholderBean;


/**
 * Template manager. Used for copy templates from one directory to another and replace placeholders with values.
 * 
 * @author mbaev
 *
 */
public class TemplateManager
{

    public void copyFilesAndReplacePlaceholders(File source, File destination, List<PlaceholderBean> placeholders)
    {
        if (source.isDirectory())
        {
            if (!destination.exists())
            {
                destination = replacePlaceholderInFileName(destination, placeholders);

                destination.mkdir();
            }

            // List all the directory contents
            String files[] = source.list();

            for (String file : files)
            {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                // Recursively searching
                copyFilesAndReplacePlaceholders(srcFile, destFile, placeholders);
            }

        }
        else
        {
            Charset charset = StandardCharsets.UTF_8;

            String content = null;
            try
            {
                content = new String(Files.readAllBytes(source.toPath()), charset);
                content = replacePlaceholderInFileContent(content, placeholders);

                destination = replacePlaceholderInFileName(destination, placeholders);
                Files.write(destination.toPath(), content.getBytes(charset));
            }
            catch (IOException e)
            {
                MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Error", "There was a problem with copy the template files");

                e.printStackTrace();
            }

        }
    }



    /*
     * Replace placeholders in name for a given file
     */
    public File replacePlaceholderInFileName(File file, List<PlaceholderBean> placeholders)
    {
        SearchManager searchManager = new SearchManager();

        String filename = file.getName();

        filename = searchManager.removeExtensionFromFile(filename);

        List<String> foundedMatches = searchManager.search(filename);

        for (PlaceholderBean placeholder : placeholders)
        {
            for (String foundMatch : foundedMatches)
            {
                if (placeholder.getPlaceholder().equals(foundMatch))
                {
                    String escapedPlaceholder = searchManager.escapeName(placeholder.getPlaceholder().toString());

                    String oldPath = file.getPath();

                    String newPath = oldPath.replaceAll(
                            escapedPlaceholder,
                            placeholder.getValue().toString());

                    return new File(newPath);
                }

            }
        }

        return file;
    }



    /*
     * Replace placeholders in content for a given file
     */
    public String replacePlaceholderInFileContent(String content, List<PlaceholderBean> placeholders)
    {
        SearchManager searchManager = new SearchManager();

        List<String> foundMathes = searchManager.search(content);

        for (PlaceholderBean placeholder : placeholders)
        {
            for (String foundMatch : foundMathes)
            {
                if (foundMatch.equals(placeholder.getPlaceholder().toString()))
                {
                    String escapedPlaceholder = searchManager.escapeName(placeholder.getPlaceholder().toString());

                    content = content.replaceAll(
                            escapedPlaceholder,
                            placeholder.getValue().toString());
                }
            }
        }


        return content;
    }


}
