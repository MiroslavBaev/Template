/**
 * 
 */
package mb.template.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract Bean
 * 
 * @author mbaev
 *
 */
public abstract class AbstractBean implements IBean
{
    private transient PropertyChangeSupport changes;



    protected AbstractBean()
    {
        this.changes = new PropertyChangeSupport(this);
    }


    @Override
    public final void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.changes.addPropertyChangeListener(listener);
    }



    @Override
    public final void removePropertyChangeListener(PropertyChangeListener listener)
    {
        this.changes.removePropertyChangeListener(listener);
    }


    @Override
    public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        this.changes.addPropertyChangeListener(propertyName, listener);
    }



    @Override
    public final void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {
        this.changes.removePropertyChangeListener(propertyName, listener);
    }



    protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        this.changes.firePropertyChange(propertyName, oldValue, newValue);
    }


}
