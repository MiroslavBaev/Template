/**
 * 
 */
package mb.template.placeholder.commands;

/**
 * @author mbaev
 *
 */
public class UppercaseCommand implements ICommand
{
    private final String name = "uppercase";



    @Override
    public String modify(String text)
    {
        if (text == null)
        {
            return null;
        }

        return text.toUpperCase();
    }



    @Override
    public String getName()
    {
        return name;
    }
}
