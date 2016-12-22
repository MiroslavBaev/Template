/**
 * 
 */
package mb.template.placeholder.commands;

/**
 * @author mbaev
 *
 */
public interface ICommand
{
    String getName();
    
    String modify(String text);
}
