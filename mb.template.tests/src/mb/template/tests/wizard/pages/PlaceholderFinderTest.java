/**
 * 
 */
package mb.template.tests.wizard.pages;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import mb.template.manager.FileManager;
import mb.template.placeholder.PlaceholderFinder;


/**
 * @author mbaev
 *
 */
public class PlaceholderFinderTest
{
    
    @Test
    public void FindPlaceholdersInTemplateWithoutFolderInsideTest()
    {
        PlaceholderFinder placeholderFinder = new PlaceholderFinder();
        //
        FileManager fileManager = new FileManager();

        // Recursive searching.Input - path Output - files
        ArrayList<File> files = fileManager.searchFilesInDirectory(new File("templates"), new NullProgressMonitor());
        List<String> allFoundPlaceholders = placeholderFinder.search(files, new NullProgressMonitor());
        //
        int actualSize = allFoundPlaceholders.size();
        int expectedSize = 2;
        //
        assertEquals(expectedSize, actualSize);
    }
    
}
