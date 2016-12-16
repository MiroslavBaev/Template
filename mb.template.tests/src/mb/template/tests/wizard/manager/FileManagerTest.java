/**
 * 
 */
package mb.template.tests.wizard.manager;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import mb.template.manager.FileManager;

/**
 * @author mbaev
 *
 */
public class FileManagerTest
{

    @Test
    public void SearchAllFilesInGivenDirectory()
    {
        FileManager fileManager = new FileManager();
        File directory = new File("templates");
        //
        List<File> files = fileManager.searchFilesInDirectory(directory,new NullProgressMonitor());
        //
        int actualSize = files.size();
        int expectedSize = 2;
        //
        assertEquals(expectedSize, actualSize);
    }

}
