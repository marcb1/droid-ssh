package marc.scp.databaseutils;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.table.TableUtils;

/*
    This file has been checked
 */

public class Database
{
    private DatabaseHelper helper;

    static private final String Log = "Database";
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

    //getters
    public List<Preference> getAllPreferences()
    {
        List<Preference> prefs = null;
        try
        {
            prefs = helper.getPreferenceDao().queryForAll();
        }
        catch (SQLException e)
        {
            android.util.Log.e(Log, "getAllPreferences exception", e);
        }
        return prefs;
    }

    public List<HostKeys> getAllHostKeys()
    {
        List<HostKeys> keys = null;
        try
        {
            keys = helper.getHostDao().queryForAll();
        }
        catch (SQLException e)
        {
            android.util.Log.e(Log, "getAllHostKeys exception", e);
        }
        return keys;
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
            android.util.Log.e(Log, "getAllFileSync exception", e);
        }
        return sync;
    }

    public void addHostKey(HostKeys h)
    {
        try
        {
            helper.getHostDao().create(h);
        }
        catch (SQLException e)
        {
            android.util.Log.e(Log, "addHostKey exception", e);
        }
    }

    public void addPreference(Preference p)
    {
        try
        {
            helper.getPreferenceDao().create(p);
        }
        catch (SQLException e)
        {
            android.util.Log.e(Log, "addPreference exception", e);
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
            android.util.Log.e(Log, "addFileSync exception", e);
        }
    }

    public void deleteFileSync(FileSync p)
    {
        try
        {
            helper.getSyncDao().delete(p);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "deleteFileSync exception", e);
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
            android.util.Log.e(Log, "deletePreference exception", e);
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

    public Preference getPreferenceID(int preferenceID)
    {
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


    public void deleteAllHostKeys()
    {
        helper.clearHostKeysTable();
    }

    public void clearAllTables()
    {
        helper.clearHostKeysTable();
        helper.clearConnectionsTable();
        helper.clearSyncTable();
    }

    public void deleteFileSyncsReferencingPref(int prefId)
    {
        try
        {
            List<FileSync> files = helper.getSyncDao().queryForAll();
            for(FileSync file: files)
            {
                if(file.getPreferencesId() == prefId)
                {
                    System.out.println(file.getId());
                    helper.getSyncDao().delete(file);
                }
            }
        }
        catch (SQLException e)
        {
            android.util.Log.e(Log, "deleteFileSyncsReferencingPref exception", e);
        }
    }
}
