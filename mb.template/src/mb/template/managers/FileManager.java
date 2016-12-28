package mb.template.managers;

import java.io.File;
import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * File manager. Search for source and header files in given directory
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
     * Recursively files searching
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

}
