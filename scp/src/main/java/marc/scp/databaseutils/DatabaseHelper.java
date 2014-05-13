package marc.scp.databaseutils;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import android.util.Log;

/**
 * Created by Marc on 5/2/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "android_ssh.db";
    private static final int DATABASE_VERSION = 1;

    private static final String log = "DatabaseHelper";

    //dao object to access table
    private Dao<Preference, Integer> preferenceDao = null;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        try
        {
            android.util.Log.d(log, "onCreate");
            TableUtils.createTable(connectionSource, Preference.class);
        }
        catch (SQLException e)
        {
            android.util.Log.d(log, "Can't create database", e);
        }
        android.util.Log.d(log, "created table in onCreate");
    }

    @Override
    public void close()
    {
        super.close();
        preferenceDao = null;
    }

    @Override
    //todo add when upgrading
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
     /*   try
        {
            android.util.Log.d(log, "onUpgrade");
        }
        catch (SQLException e)
        {
            android.util.Log.d(log, "exception caught while upgrading");
            throw new RuntimeException(e);
        }*/
    }


    public Dao<Preference, Integer> getPreferenceDao()
    {
        if (preferenceDao == null)
        {
            try
            {
                preferenceDao = getDao(Preference.class);
            }
            catch (java.sql.SQLException e)
            {
                android.util.Log.d(log, "Can't get preferenceDao " + e.getMessage());
            }
        }
        return preferenceDao;
    }
}
