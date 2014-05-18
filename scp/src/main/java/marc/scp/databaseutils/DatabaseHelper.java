package marc.scp.databaseutils;

import java.sql.SQLException;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by Marc on 5/2/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "whomarc_ssh.db";
    private static final int DATABASE_VERSION = 1;

    private static final String log = "DatabaseHelper";

    //DAO object to access table
    private Dao<Preference, Integer> preferenceDao = null;
    private Dao<HostKeys, Integer> hostKeysDao = null;
    private Dao<FileSync, Integer> syncDao = null;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //DAO getters
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
                android.util.Log.d(log, "getPreferenceDao, exception", e);
            }
        }
        return preferenceDao;
    }

    public Dao<HostKeys, Integer> getHostDao()
    {
        if (hostKeysDao == null)
        {
            try
            {
                hostKeysDao = getDao(HostKeys.class);

            }
            catch (java.sql.SQLException e)
            {
                android.util.Log.d(log, "getHostDao exception ", e);
            }
        }
        return hostKeysDao;
    }

    public Dao<FileSync, Integer> getSyncDao()
    {
        if (syncDao == null)
        {
            try
            {
                syncDao = getDao(FileSync.class);
            }
            catch (java.sql.SQLException e)
            {
                android.util.Log.d(log, "getSyncDao exception", e);
            }
        }
        return syncDao;
    }

    public void clearHostKeysTable()
    {
        try
        {
            TableUtils.clearTable(connectionSource, HostKeys.class);
        }
        catch (SQLException e)
        {
            android.util.Log.d(log, "clearHostKeysTable exception", e);
        }
    }

    public void clearSyncTable()
    {
        try
        {
            TableUtils.clearTable(connectionSource, FileSync.class);
        }
        catch (SQLException e)
        {
            android.util.Log.e(log, "clearSyncTable exception", e);
        }
    }

    public void clearConnectionsTable()
    {
        try
        {
            TableUtils.clearTable(connectionSource, Preference.class);
        }
        catch (SQLException e)
        {
            android.util.Log.e(log, "clearConnectionsTable exception", e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        try
        {
            android.util.Log.d(log, "onCreate, creating the needed tables.");
            TableUtils.createTable(connectionSource, Preference.class);
            TableUtils.createTable(connectionSource, FileSync.class);
            TableUtils.createTable(connectionSource, HostKeys.class);
        }
        catch (SQLException e)
        {
            android.util.Log.d(log, "onCreate exception", e);
        }
        android.util.Log.d(log, "onCreate complete");
    }

    @Override
    public void close()
    {
        super.close();
        preferenceDao = null;
        syncDao = null;
        hostKeysDao = null;
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
}

