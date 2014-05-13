package marc.scp.databaseutils;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;


public class Database
{
    private final String Log = "Database";

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
            android.util.Log.d(Log, "error while querying for preferences " + e.getMessage());
        }
        return prefs;
    }

    public void addPreference(Preference p)
    {
        try
        {
            helper.getPreferenceDao().create(p);
        }
        catch (SQLException e)
        {
            android.util.Log.d(Log, "error while adding preference " + e.getMessage());
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
            android.util.Log.d(Log, "error while deleting preference " + e.getMessage());
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
            android.util.Log.d(Log, "error while updating preference " + e.getMessage());
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
            android.util.Log.d(Log, "error while getting preference ID " + e.getMessage());
        }
        return pref;
    }
}
