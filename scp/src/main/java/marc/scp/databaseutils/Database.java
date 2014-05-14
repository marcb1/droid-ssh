package marc.scp.databaseutils;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

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
            android.util.Log.d(Log, "Error while querying for preferences: " + e.getMessage());
        }
        return prefs;
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
            android.util.Log.d(Log, "Error while querying for preferences: " + e.getMessage());
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
            android.util.Log.d(Log, "Error while adding preference: " + e.getMessage());
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
            android.util.Log.d(Log, "Error while adding FileSync: " + e.getMessage());
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
            android.util.Log.d(Log, "Wrror while deleting preference: " + e.getMessage());
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
            android.util.Log.d(Log, "Error while updating preference: " + e.getMessage());
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
            android.util.Log.d(Log, "Error while getting preference ID: " + e.getMessage());
        }
        return pref;
    }
}
