package mb.template.manager;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;


/**
 * File manager. Search all files in given directory
 * 
 * @author mbaev
 *
 */
public class FileManager
{

    private ArrayList<File> files = new ArrayList<File>();

    

    /*
     * Recursively searching
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
            if (filesInDirectory[i].isDirectory())
            {
                this.files.add(filesInDirectory[i]);

                searchFilesInDirectory(filesInDirectory[i], monitor);
            }
            else
            {
                this.files.add(filesInDirectory[i]);
            }
        }
        return this.files;
    }


    
}
