/**
 * 
 */
package mb.template.placeholder.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mbaev
 *
 */
public class Commands
{
    
    private List<ICommand> allCommands = new ArrayList<>();
    
    /**
     * 
     */
    public Commands()
    {
        allCommands.add(new LowercaseCommand());
        allCommands.add(new UppercaseCommand());
    }
    
    public List<ICommand> getAllCommands()
    {
        return allCommands;
    }

}
