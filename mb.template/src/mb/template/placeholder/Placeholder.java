/**
 * 
 */
package mb.template.placeholder;

import mb.template.listeners.IChangeValueListener;
import mb.template.placeholder.commands.CommandConstants;
import mb.template.util.AbstractBean;


/**
 * This object keeps a pair placeholder - value.
 * 
 * @author mbaev
 *
 */
public class Placeholder extends AbstractBean
{
    private IChangeValueListener listener;

    private String placeholder;
    private String value;



    public Placeholder(String placeholder, String value)
    {
        this(placeholder);
        this.value = value;
        this.placeholder = null;
    }



    public Placeholder(String placeholder)
    {
        this.placeholder = placeholder;
        this.value = "";
        this.listener = null;
    }



    public String getPlaceholder()
    {
        return this.placeholder;
    }



    public void setPlaceholder(String placeholder)
    {
        firePropertyChange("placehodler", this.placeholder, this.placeholder = placeholder);
    }



    public String getValue()
    {
        return this.value;
    }



    public void setValue(String value)
    {
        firePropertyChange("value", this.value, this.value = value);

        fireSetValueChange();
    }



    public void fireSetValueChange()
    {
        this.listener.isChange();
    }



    public void addChangeValueListener(IChangeValueListener listener)
    {
        this.listener = listener;
    }



    public void removeChangeValueListener()
    {
        this.listener = null;
    }



    public String getPlaceholderWithoutCommand()
    {
        String result = null;

        int indexCommand = placeholder.indexOf(CommandConstants.COMMAND_SYMBOL);

        if (indexCommand < 0)
        {
            return placeholder;
        }

        result = placeholder.substring(CommandConstants.INDEX_PLACEHOLDER_START, CommandConstants.PLACEHOLDER_START_LENGHT);
        result = result + placeholder.substring(indexCommand + 1);

        return result;
    }



    public String getCommand()
    {
        int indexCommandSymbol = placeholder.indexOf(CommandConstants.COMMAND_SYMBOL);

        if (placeholder == null || indexCommandSymbol < 0)
        {
            return null;
        }

        return placeholder.substring(
                CommandConstants.INDEX_PLACEHOLDER_START + CommandConstants.PLACEHOLDER_START_LENGHT,
                placeholder.indexOf(CommandConstants.COMMAND_SYMBOL));

    }



    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPlaceholderWithoutCommand() == null) ? 0 : getPlaceholderWithoutCommand().hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Placeholder other = (Placeholder) obj;
        if (getPlaceholderWithoutCommand() == null)
        {
            if (other.getPlaceholderWithoutCommand() != null)
                return false;
        }
        else if (!getPlaceholderWithoutCommand().equals(other.getPlaceholderWithoutCommand()))
            return false;
        return true;
    }


}
