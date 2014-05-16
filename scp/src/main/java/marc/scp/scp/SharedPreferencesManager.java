package marc.scp.scp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

/**
 * Created by Marc on 5/16/14.
 */
public class SharedPreferencesManager
{
    static private SharedPreferences sharedPref;
    static private SharedPreferencesManager instance;

    private HashMap<String, Object> cachedPreferences;
    private final String HOSTCHECKING = "pref_compression";
    private final String FONTSIZE = "font_size";

    public static SharedPreferencesManager getInstance(Context context)
    {
        if(sharedPref == null)
        {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            instance = new SharedPreferencesManager();
        }
        return instance;
    }

    private SharedPreferencesManager()
    {
        //cachedPreferences = new HashMap<String, Object>();
    }

    public boolean compressionEnabled()
    {
       boolean enabled = sharedPref.getBoolean(HOSTCHECKING , false);
       return enabled;
    }

    public String fontSize()
    {
        String fontSize = sharedPref.getString(FONTSIZE , "0");
        return fontSize;
    }
}