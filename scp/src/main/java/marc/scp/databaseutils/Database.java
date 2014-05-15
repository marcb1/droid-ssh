package marc.scp.databaseutils;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.table.TableUtils;

public class Database
{
    private static final String Log = "Database";

    private DatabaseHelper helper;
    static private Database instance;

    private Database(Context ctx)
    {
        helper = new DatabaseHelper(ctx);
    }

    static public void init(Context ctx)
    {
        if (instance == null)
        {
            instance = new Database(ctx);
        }
    }

    static public Database getInstance()
    {
        return instance;
    }

    public List<Preference> getAllPreferences()
    {
        List<Preference> prefs = null;
        try
        {
            prefs = helper.getPreferenceDao().queryForAll();
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "getAllPreferences exception", e);
        }
        return prefs;
    }

    public List<HostKeys> getAllHostKeys()
    {
        List<HostKeys> prefs = null;
        try
        {
            prefs = helper.getHostDao().queryForAll();
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "getAllPreferences exception", e);
        }
        return prefs;
    }

    public List<HostKeys> getHostKey(String host)
    {
        List<HostKeys> res = null;
        try
        {
            res = helper.getHostDao().queryForEq("hostName", host);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "getHostKey exception", e);
        }
        return res;
    }

    public void deleteAllHostKeys()
    {
            helper.clearHostKeysTable();
    }

    public void addHostKey(HostKeys h)
    {
        try
        {
            helper.getHostDao().create(h);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "addHostKey exception", e);
        }
    }

    public List<FileSync> getAllFileSync()
    {
        List<FileSync> sync = null;
        try
        {
            sync = helper.getSyncDao().queryForAll();
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "getAllFileSync exception", e);
        }
        return sync;
    }

    public void addPreference(Preference p)
    {
        try
        {
            helper.getPreferenceDao().create(p);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "addPreference exception", e);
        }
    }

    public void addFileSync(FileSync f)
    {
        try
        {
            helper.getSyncDao().create(f);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "addFileSync exception", e);
        }
    }

    public void deleteSync(FileSync p)
    {
        try
        {
            helper.getSyncDao().delete(p);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "deletePreference exception", e);
        }
    }


    public void deletePreference(Preference p)
    {
        try
        {
            helper.getPreferenceDao().delete(p);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "deletePreference exception", e);
        }
    }

    public void updatePreference(Preference p)
    {
        try
        {
            helper.getPreferenceDao().update(p);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "updatePreference exception", e);
        }
    }

    public Preference getPreferenceID(int preferenceID) {
        Preference pref = null;
        try
        {
            pref = helper.getPreferenceDao().queryForId(preferenceID);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "getPreferenceID exception", e);
        }
        return pref;
    }
}
