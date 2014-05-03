package marc.scp.scp;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by Marc on 5/2/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "marc_ssh.db";
    private static final int DATABASE_VERSION = 1;

    private static final String log = "DatabaseHelper";//DatabaseHelper.class.getName()

    private Dao<Preferences, Integer> simpleDao = null;
    private RuntimeExceptionDao<Preferences, Integer> simpleRuntimeDao = null;

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
            TableUtils.createTable(connectionSource, Preferences.class);
        }
        catch (SQLException e)
        {
            Log.e(log, "Can't create database", e);
        }
        Log.i(log, "created new entries in onCreate");
    }

    public void addToTable(String a)
    {
        //here we try inserting data in the on-create as a test
        RuntimeExceptionDao<Preferences, Integer> dao = getSimpleDataDao();
        Preferences pref = new Preferences(a, a, a, 1);
        dao.create(pref);
    }

    public Dao<Preferences, Integer> getDao() throws SQLException
    {
        if (simpleDao == null)
        {
            simpleDao = getDao(Preferences.class);
        }
        return simpleDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Preferences, Integer> getSimpleDataDao()
    {
        if (simpleRuntimeDao == null)
        {
            //simpleRuntimeDao = getRuntimeExceptionDao(Preferences.class);
        }
        return simpleRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close()
    {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            Log.i(log, "onUpgrade");
            TableUtils.dropTable(connectionSource, Preferences.class, true);
            //after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        }
        catch (SQLException e)
        {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }
}
