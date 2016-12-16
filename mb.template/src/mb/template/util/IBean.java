/**
 * 
 */
package mb.template.util;

import java.beans.PropertyChangeListener;

/**
 * IBean
 * 
 * @author mbaev
 *
 */
public interface IBean
{
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}