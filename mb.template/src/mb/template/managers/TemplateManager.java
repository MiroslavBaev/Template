package mb.template.managers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import mb.template.placeholder.PlaceholderBean;
import mb.template.placeholder.PlaceholderPattern;
import mb.template.placeholder.commands.Commands;
import mb.template.placeholder.commands.ICommand;


/**
 * Template manager. Used for copy templates from one directory to another and replace placeholders with values.
 * 
 * @author mbaev
 *
 */
public class TemplateManager
{
    private PatternSearchManager searchManager;

    private Commands allCommands;



    public TemplateManager()
    {
        this.searchManager = new PatternSearchManager(new PlaceholderPattern().getPatterns());
        this.allCommands = new Commands();
    }



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
     * 
     * Replace placeholders in name for a given file
     * 
     */
    public File replacePlaceholderInFileName(File file, List<PlaceholderBean> placeholders)
    {
        String filename = file.getName();

        filename = searchManager.removeExtensionFromFile(filename);


        if (placeholders.size() <= 0)
        {
            return file;
        }

        String newPath = null;
        String oldPath = file.getPath();

        for (PlaceholderBean placeholder : placeholders)
        {
            String modifiedValue = processCommand(placeholder);

            String escapedPlaceholder = searchManager.escapeName(placeholder.getPlaceholder().toString());

            newPath = oldPath.replaceAll(
                    escapedPlaceholder,
                    modifiedValue);
            
            oldPath = newPath;
        }
        return new File(newPath);
    }



    /*
     * 
     * Replace placeholders in content for a given file
     * 
     */
    public String replacePlaceholderInFileContent(String content, List<PlaceholderBean> placeholders)
    {
        for (PlaceholderBean placeholder : placeholders)
        {
            String modifiedValue = processCommand(placeholder);
            
            String escapedPlaceholder = searchManager.escapeName(placeholder.getPlaceholder());

            content = content.replaceAll(
                    escapedPlaceholder,
                    modifiedValue);
        }

        return content;
    }



    /*
     * 
     * Get command from placeholder and modify placeholder value;
     * 
     */
    private String processCommand(PlaceholderBean placeholder)
    {
        if (placeholder.getCommand() != null)
        {
            for (ICommand command : allCommands.getAllCommands())
            {
                if (placeholder.getCommand().equals(command.getName()))
                {
                    return command.modify(placeholder.getValue());
                }
            }
        }
        return placeholder.getValue();
    }


}
