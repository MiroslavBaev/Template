package mb.template.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final static String PLACEHOLDER_REGEX = "\\$\\{(.*?)\\}";



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
                MessageDialog.openInformation
                (Display.getCurrent().getActiveShell(),"Error" ,"There was a problem with copy the template files" );
                
                e.printStackTrace();
            }
           
        }
    }
    


    /*
     * Replace placeholders in name for a given file
     */
    public File replacePlaceholderInFileName(File file, List<PlaceholderBean> placeholders)
    {
        String filename = file.getName().toString();

        filename = removeExtensionFromFile(filename);

        for (PlaceholderBean placeholder : placeholders)
        {
            if (placeholder.getPlaceholder().equals(filename))
            {
                String oldPath = file.getPath();

                String newPath = oldPath.replaceAll(
                        PLACEHOLDER_REGEX,
                        placeholder.getValue().toString());

                return new File(newPath);
            }

        }
        return file;
    }



    /*
     * Replace placeholders in content for a given file
     */
    public String replacePlaceholderInFileContent(String content, List<PlaceholderBean> placeholders)
    {
        Pattern pattern = Pattern.compile(PLACEHOLDER_REGEX);
        Matcher matcher = null;

        matcher = pattern.matcher(content);

        while (matcher.find())
        {
            for (PlaceholderBean placeholder : placeholders)
            {
                if (matcher.group().equals(placeholder.getPlaceholder().toString()))
                {
                    String escapedPlaceholder = escapePlaceholder(placeholder.getPlaceholder().toString());

                    content = content.replaceAll(
                            escapedPlaceholder,
                            placeholder.getValue().toString());
                }
            }

        }

        return content;
    }


    /*
     * Remove extension from file name
     */
    private String removeExtensionFromFile(String filename)
    {
        String DOT = ".";

        if (filename.lastIndexOf(DOT) > 0)
        {
            filename = filename.substring(0, filename.lastIndexOf(DOT));

            return filename;
        }

        return filename;
    }


    /*
     * Escape placeholder
     */
    private String escapePlaceholder(String placeholder)
    {
        char openBracket = '{';
        char closeBracket = '}';
        char specialSymbol = '$';
        String escapeSymbol = "\\";

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < placeholder.length(); i++)
        {
            if (placeholder.charAt(i) == openBracket
                    || placeholder.charAt(i) == closeBracket
                    || placeholder.charAt(i) == specialSymbol)
            {
                result.append(escapeSymbol + placeholder.charAt(i));
            }
            else
            {
                result.append(placeholder.charAt(i));
            }

        }

        return result.toString();
    }


}
