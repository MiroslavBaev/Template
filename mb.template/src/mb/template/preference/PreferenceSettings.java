package mb.template.preference;


import org.osgi.service.prefs.Preferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import mb.template.Activator;


public class PreferenceSettings
{
    private Preferences preferences;



    public PreferenceSettings()
    {
        this.preferences = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
    }



    public void saveSetting(String key, String path)
    {
        if(key == null || path == null)
        {
            return;
        }
        
        this.preferences.put(key, path);

        try
        {
            this.preferences.flush();
        }
        catch (BackingStoreException e)
        {
            e.printStackTrace();
        }
    }



    public String loadSetting(String key)
    {
        if (!preferenceKeyIsExist(key))
        {
            return null;
        }

        return this.preferences.get(key, "default");
    }



    private boolean preferenceKeyIsExist(String desiredKey)
    {
        String[] keys = null;

        try
        {
            keys = this.preferences.keys();
        }
        catch (BackingStoreException e)
        {
            e.printStackTrace();
        }

        for (String key : keys)
        {
            if (key.equals(desiredKey))
            {
                return true;
            }
        }

        return false;
    }
}
