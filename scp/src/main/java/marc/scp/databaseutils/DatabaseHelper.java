package marc.scp.databaseutils;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import marc.scp.scp.R;

/**
 * Created by Marc on 5/2/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "android_ssh.db";
    private static final int DATABASE_VERSION = 1;

    private static final String log = "DatabaseHelper";//or DatabaseHelper.class.getName()

    //dao object to access table
    private Dao<Preference, Integer> preferenceDao = null;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        try
        {
            Log.i(log, "onCreate");
            TableUtils.createTable(connectionSource, Preference.class);
        }
        catch (SQLException e)
        {
            Log.e(log, "Can't create database", e);
        }
        Log.i(log, "created new entries in onCreate");
    }



    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close()
    {
        super.close();
        preferenceDao = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            Log.i(log, "onUpgrade");
            TableUtils.dropTable(connectionSource, Preference.class, true);
            //after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        }
        catch (SQLException e)
        {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public Dao<Preference, Integer> getPreferenceDao() {
        if (preferenceDao == null) {
            try {
                preferenceDao = getDao(Preference.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return preferenceDao;
    }

}
