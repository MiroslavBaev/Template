/**
 * 
 */
package mb.template.placeholder;

import mb.template.listeners.IChangeValueListener;
import mb.template.util.AbstractBean;


/**
 * This object keeps a pair placeholder - value.
 * 
 * @author mbaev
 *
 */
public class PlaceholderBean extends AbstractBean
{
    private IChangeValueListener listener;

    private String placeholder;
    private String value;



    public PlaceholderBean(String placeholder, String value)
    {
        this(placeholder);
        this.value = value;
    }



    public PlaceholderBean(String placeholder)
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



    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((placeholder == null) ? 0 : placeholder.hashCode());
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
        PlaceholderBean other = (PlaceholderBean) obj;
        if (placeholder == null)
        {
            if (other.placeholder != null)
                return false;
        }
        else if (!placeholder.equals(other.placeholder))
            return false;
        return true;
    }


}
