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
import mb.template.placeholder.Placeholder;
import mb.template.placeholder.PlaceholderPattern;
import mb.template.placeholder.commands.Commands;
import mb.template.placeholder.commands.ICommand;


/**
 * Placeholder finder. Search placeholders in given directory /in file name and file content/
 * 
 * @author mbaev
 *
 */
public class PlaceholderManager
{
    private SearchManager searchManager;
    private Commands allCommands;



    public PlaceholderManager()
    {
        this.searchManager = new SearchManager(new PlaceholderPattern().getPatterns());
        this.allCommands = new Commands();
    }



    /*
     * Search placeholder in file names and file contents
     */
    public ArrayList<String> searchPlaceholdersInFiles(List<File> files, IProgressMonitor monitor)
    {
        ArrayList<String> allFoundPlaceholders = new ArrayList<>();
        List<String> foundedMatches = null;

        Charset charset = StandardCharsets.UTF_8;

        for (int i = 0; i < files.size(); i++)
        {
            if (monitor.isCanceled())
            {
                return new ArrayList<>();
            }

            // Search in filename
            String fileNameWithoutExtension = this.searchManager.removeExtensionFromFile(files.get(i).getName());

            foundedMatches = this.searchManager.search(fileNameWithoutExtension);

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

                    foundedMatches = this.searchManager.search(content);
                }
                catch (IOException e)
                {

                    Display.getDefault().syncExec(new Runnable()
                    {
                        public void run()
                        {
                            MessageDialog.openError(Display.getDefault().getActiveShell(),
                                    "Error", "There was a problem with reading the template files");
                        }
                    });

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



    public void copyFilesAndReplacePlaceholders(File source, File destination, List<Placeholder> placeholders)
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
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        "Error", "There was a problem with copy the template files");

                e.printStackTrace();
            }

        }
    }



    /*
     * Replace placeholders in name for a given file
     */
    public File replacePlaceholderInFileName(File file, List<Placeholder> placeholders)
    {
        String filename = file.getName();

        filename = this.searchManager.removeExtensionFromFile(filename);


        if (placeholders.size() <= 0)
        {
            return file;
        }

        String newPath = null;
        String oldPath = file.getPath();

        for (Placeholder placeholder : placeholders)
        {
            String modifiedValue = processCommand(placeholder);

            String escapedPlaceholder = this.searchManager.escapeName(placeholder.getPlaceholder().toString());

            newPath = oldPath.replaceAll(
                    escapedPlaceholder,
                    modifiedValue);

            oldPath = newPath;
        }
        return new File(newPath);
    }



    /*
     * Replace placeholders in content for a given file
     */
    public String replacePlaceholderInFileContent(String content, List<Placeholder> placeholders)
    {
        for (Placeholder placeholder : placeholders)
        {
            String modifiedValue = processCommand(placeholder);

            String escapedPlaceholder = this.searchManager.escapeName(placeholder.getPlaceholder());

            content = content.replaceAll(
                    escapedPlaceholder,
                    modifiedValue);
        }

        return content;
    }



    /*
     * Get command from placeholder and modify placeholder value;
     */
    private String processCommand(Placeholder placeholder)
    {
        if (placeholder.getCommand() != null)
        {
            for (ICommand command : this.allCommands.getAllCommands())
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
