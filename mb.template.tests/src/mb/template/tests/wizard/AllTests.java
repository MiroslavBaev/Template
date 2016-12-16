/**
 * 
 */
package mb.template.tests.wizard;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import mb.template.tests.wizard.manager.FileManagerTest;
import mb.template.tests.wizard.manager.TemplateManagerTest;
import mb.template.tests.wizard.pages.PlaceholderFinderTest;


/**
 * @author mbaev
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TemplateManagerTest.class, PlaceholderFinderTest.class, FileManagerTest.class })
public class AllTests
{

}
