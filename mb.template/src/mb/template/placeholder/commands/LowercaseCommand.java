/**
 * 
 */
package mb.template.placeholder.commands;

/**
 * @author mbaev
 *
 */
public class LowercaseCommand implements ICommand
{

    private final static String name = "lowercase";



    @Override
    public String modify(String text)
    {
        if (text == null)
        {
            return null;
        }

        return text.toLowerCase();
    }



    @Override
    public String getName()
    {
        return name;
    }

}
