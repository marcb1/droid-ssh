package marc.scp.sshutils;

import android.util.Log;

import java.util.Hashtable;
/**
 * Created by marc on 12/6/2015.
 */
public class SshLogger implements com.jcraft.jsch.Logger
{
    private Hashtable<Integer, String>       _logLevels;

    public SshLogger()
    {
        _logLevels = new Hashtable<Integer, String>();
        // Mapping between JSch levels and our own levels
        _logLevels.put(new Integer(DEBUG), "DEBUG: ");
        _logLevels.put(new Integer(INFO), "INFO: ");
        _logLevels.put(new Integer(WARN), "WARN: ");
        _logLevels.put(new Integer(ERROR), "ERROR: ");
        _logLevels.put(new Integer(FATAL), "FATAL: ");

    }

    @Override
    public boolean isEnabled(int pLevel)
    {
        // all levels enabled
        return true;
    }

    @Override
    public void log(int pLevel, String message)
    {
        String level = _logLevels.get(pLevel);
        if (level == null) {
            level = "unknown";
        }
        Log.d("DROID_SSH.SshLogger", message);
    }
}

