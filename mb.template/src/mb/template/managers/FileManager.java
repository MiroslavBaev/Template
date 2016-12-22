package mb.template.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;


/**
 * File manager. Search all files in given directory
 * 
 * @author mbaev
 *
 */
public class FileManager
{
    private final static String C_FILE = ".c";
    private final static String H_FILE = ".h";

    private ArrayList<File> files = new ArrayList<File>();



    /*
     * 
     * Recursively files searching
     * 
     */
    public ArrayList<File> searchFilesInDirectory(File directory, IProgressMonitor monitor)
    {
        File[] filesInDirectory = directory.listFiles();

        if (filesInDirectory == null || monitor.isCanceled())
        {
            return null;
        }

        for (int i = 0; i < filesInDirectory.length; i++)
        {
            if (filesInDirectory[i].toString().endsWith(C_FILE) ||
                    filesInDirectory[i].toString().endsWith(H_FILE))
            {
                if (filesInDirectory[i].isDirectory())
                {
                    this.files.add(filesInDirectory[i]);

                    searchFilesInDirectory(filesInDirectory[i], monitor);
                }
                else if (filesInDirectory[i].isFile())
                {
                    this.files.add(filesInDirectory[i]);
                }
            }
        }
        return this.files;
    }



    /*
     * 
     * Remove full directory to folder. Get only parent folder name. Input full path to project, output only name of project.
     *
     * Example:
     * C:\Users\MBaev.HILSCHERDTC\Desktop\runtime-EclipseApplication\Test - > Test
     * 
     */
    public String getLastDirectoryChildNameFromPath(String projectFolderPath)
    {
        if (projectFolderPath == null)
        {
            return null;
        }

        if (projectFolderPath.contains("/"))
        {
            projectFolderPath = projectFolderPath.replace("/", "\\");
        }

        projectFolderPath = removeLastChildFileFromDirectoryPath(projectFolderPath);

        String[] splittedPath = projectFolderPath.split(Pattern.quote(File.separator));

        return splittedPath[splittedPath.length - 1];
    }



    /*
     *
     * Remove file from directory path if exist
     * 
     * Example:
     * C:/ProgramFiles/Work/file.exe - > C:/ProgramFiles/Work
     * 
     */
    public String removeLastChildFileFromDirectoryPath(String fullPath)
    {
        if (fullPath == null)
        {
            return null;
        }
        if (new File(fullPath).isFile())
        {
            char separator = '\\';

            int lastSeparatorIndex = fullPath.lastIndexOf(separator);

            return fullPath.substring(0, lastSeparatorIndex);
        }
        else
        {
            return fullPath;
        }
    }

}
