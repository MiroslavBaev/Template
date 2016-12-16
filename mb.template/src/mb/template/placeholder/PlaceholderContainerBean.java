/**
 * 
 */
package mb.template.placeholder;

import java.util.ArrayList;
import java.util.List;

import mb.template.util.AbstractBean;



/**
 * All placeholder objects are kept in this object
 * 
 * @author mbaev
 *
 */
public class PlaceholderContainerBean extends AbstractBean
{
    private List<PlaceholderBean> placeholders;



    public PlaceholderContainerBean()
    {
        this.placeholders = new ArrayList<>();;
    }



    public void addPlaceholder(PlaceholderBean placeholder)
    {
        if(placeholders.contains(placeholder))
        {
            return;
        }
        
        List<PlaceholderBean> newPlaceholders = new ArrayList<>(this.placeholders);
        
        newPlaceholders.add(placeholder);

        setPlaceholders(newPlaceholders);
    }



    public void removePlaceholder(PlaceholderBean placeholder)
    {
        List<PlaceholderBean> newPlaceholders = new ArrayList<>(this.placeholders);
        
        newPlaceholders.remove(placeholder);

        setPlaceholders(newPlaceholders);
    }



    public List<PlaceholderBean> getPlaceholders()
    {
        return this.placeholders;
    }



    public void setPlaceholders(List<PlaceholderBean> placeholders)
    {
        firePropertyChange("placeholders", this.placeholders, this.placeholders = placeholders);
    }



    public void clear()
    {
        setPlaceholders(new ArrayList<>());

        // clear list and dispose listeners on placeholder objects.
        this.placeholders.clear();
    }



    

}