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
public class PlaceholderContainer extends AbstractBean
{
    private List<Placeholder> placeholders;



    public PlaceholderContainer()
    {
        this.placeholders = new ArrayList<>();
        ;
    }



    public void addPlaceholder(Placeholder placeholder)
    {
        if (placeholders.contains(placeholder))
        {
            return;
        }

        List<Placeholder> newPlaceholders = new ArrayList<>(this.placeholders);

        newPlaceholders.add(placeholder);

        setPlaceholders(newPlaceholders);
    }



    public void removePlaceholder(Placeholder placeholder)
    {
        List<Placeholder> newPlaceholders = new ArrayList<>(this.placeholders);

        newPlaceholders.remove(placeholder);

        setPlaceholders(newPlaceholders);
    }



    public List<Placeholder> getPlaceholders()
    {
        return this.placeholders;
    }



    public void setPlaceholders(List<Placeholder> placeholders)
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