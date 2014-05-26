package marc.scp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import jackpal.androidterm.emulatorview.ColorScheme;
import marc.scp.constants.Constants;
import marc.scp.sshutils.SshConnection;
import marc.scp.terminal.TerminalSession;
import marc.scp.terminal.TerminalView;

/**
 * Created by Marc on 5/16/14.
 */
public class SharedPreferencesManager
{
    private SharedPreferences sharedPref;
    static private SharedPreferencesManager instance;

    //Not sure if it is a good idea to cache preferences
    // private HashMap<String, Object> cachedPreferences;

    static private final String HOSTCHECKING = "pref_host_checking";

    static private final String COMPRESSION = "pref_compression";
    static private final String COMPRESSIONVALUES = "compression_value";

    static private final String FONTSIZE = "font_size";
    static private final String TERMINALEMULATION = "terminal_emulation";
    static public final String DELETETABLES = "delete_tables";

    public static void init(Context context)
    {
        if(instance == null)
        {
            instance = new SharedPreferencesManager(context);
        }
    }

    public static SharedPreferencesManager getInstance()
    {
        return instance;
    }

    private SharedPreferencesManager(Context context)
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //cachedPreferences = new HashMap<String, Object>();
    }

    //Preference getters
    public boolean hostChecking()
    {
        boolean enabled = sharedPref.getBoolean(HOSTCHECKING , false);
        return enabled;
    }

    public boolean compressionEnabled()
    {
       boolean enabled = sharedPref.getBoolean(COMPRESSION , false);
       return enabled;
    }

    public String getCompressionValue()
    {
        String compression = sharedPref.getString(COMPRESSIONVALUES, null);
        return compression;
    }

    public String fontSize()
    {
        String fontSize = sharedPref.getString(FONTSIZE , "10");
        return fontSize;
    }

    public String getTerminalEmulation()
    {
        String terminal = sharedPref.getString(TERMINALEMULATION, "vt100");
        return terminal;
    }

    public void setPreferencesonShellConnection(SshConnection conn)
    {
        String compression = getCompressionValue();
        if(compression != null)
        {
            conn.enableCompression(compression);
        }

        boolean hostChecking = hostChecking();
        if(hostChecking)
        {
            conn.disableHostChecking();
        }
    }

    public void setPreferencesTerminal(TerminalView terminal)
    {
        terminal.setTermType(getTerminalEmulation());
        int textSize = Integer.parseInt(fontSize());
        terminal.setTextSize(textSize);
        terminal.setAltSendsEsc(false);
        terminal.setMouseTracking(true);
       // terminal.setColorScheme(new ColorScheme(Constants.COLOR_SCHEMES[2]));
    }

    public void setPreferenceSession(TerminalSession terminalSession)
    {
        terminalSession.setDefaultUTF8Mode(true);
    }
}