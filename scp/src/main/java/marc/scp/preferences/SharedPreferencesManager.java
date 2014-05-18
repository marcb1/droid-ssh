package marc.scp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

import marc.scp.sshutils.SshConnection;

/**
 * Created by Marc on 5/16/14.
 */
public class SharedPreferencesManager
{
    static private SharedPreferences sharedPref;
    static private SharedPreferencesManager instance;

    private HashMap<String, Object> cachedPreferences;

    static private final String HOSTCHECKING = "pref_host_checking";
    static private final String COMPRESSION = "pref_compression";
    static private final String COMPRESSIONVALUES = "compression_value";
    static private final String FONTSIZE = "font_size";
    static private final String TERMINALEMULATION = "terminal_emulation";
    static public final String DELETETABLES = "delete_tables";
    static public final String THEME = "pref_theme";


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
       boolean enabled = sharedPref.getBoolean(COMPRESSION , false);
       return enabled;
    }

    public String fontSize()
    {
        String fontSize = sharedPref.getString(FONTSIZE , "10");
        return fontSize;
    }


    public void setPreferencesonConnection(SshConnection conn)
    {
        final String NONE = "NONE";
        String compression = sharedPref.getString(COMPRESSIONVALUES, NONE);
        if(!compression.equals(NONE))
        {
            conn.enableCompression(compression);
        }
    }
}