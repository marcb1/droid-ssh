package marc.scp.databaseutils;



        import java.sql.SQLException;
        import java.util.List;

        import android.content.Context;
        import android.content.Context;



public class Database
{
    private DatabaseHelper helper;
    private Database(Context ctx)
    {
        helper = new DatabaseHelper(ctx);
    }

    static private Database instance;
    static public void init(Context ctx)
    {
        if (instance == null) {
            instance = new Database(ctx);
        }
    }

    static public Database getInstance()
    {
        return instance;
    }



    private DatabaseHelper getHelper()
    {
        return helper;
    }

    public List<Preference> getAllPreferences()
    {
        List<Preference> prefs = null;
        try
        {
            prefs = getHelper().getPreferenceDao().queryForAll();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return prefs;
    }

    public void addPrefst(Preference p)
    {
        try {
            getHelper().getPreferenceDao().create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Preference newPerference()
    {
        Preference p = new Preference();
        try {
            getHelper().getPreferenceDao().create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void deletePreference(Preference p)
    {
        try {
            getHelper().getPreferenceDao().delete(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWishItem(Preference p)
    {
        try {
            getHelper().getPreferenceDao().update(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshWishList(Preference p)
    {
        try {
            getHelper().getPreferenceDao().refresh(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
