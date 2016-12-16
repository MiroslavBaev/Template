/**
 * 
 */
package mb.template.tests.wizard.manager;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import mb.template.manager.TemplateManager;
import mb.template.placeholder.PlaceholderBean;


/**
 * @author mbaev
 *
 */
public class TemplateManagerTest
{

    @Test
    public void ReplacePlaceholderInFileNameTest()
    {
        TemplateManager templateManager = new TemplateManager();
        //
        List<PlaceholderBean> placeholderBeans = new ArrayList<>();
        placeholderBeans.add(new PlaceholderBean("${module_name}","mymodule"));
        //
        File input = new File("example\\${module_name}");
        //
        File actualFile = templateManager.replacePlaceholderInFileName(input, placeholderBeans);
        File expectedFile = new File("example\\mymodule");
        //
        assertEquals(expectedFile, actualFile);
    }



    @Test
    public void ReplacePlaceholderInFileContentTest()
    {
        TemplateManager templateManager = new TemplateManager();
        //
        List<PlaceholderBean> placeholderBeans = new ArrayList<>();
        placeholderBeans.add(new PlaceholderBean("${module_name}","mymodule"));
        placeholderBeans.add(new PlaceholderBean("${my_program}","myprogram"));
        //
        String input = new String("${module_name}" + " " + "${my_program}");
        //
        String actualContent = templateManager.replacePlaceholderInFileContent(input, placeholderBeans);
        String expectedContent = "mymodule myprogram";
        //
        assertEquals(expectedContent, actualContent);
    }

}
