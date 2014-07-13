package marc.scp.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import marc.scp.fileutils.FileUtils;

/**
 * Created by Marc on 6/15/2014.
 */
public class Database
{
    public static String DB_FILEPATH = "/data/data/{package_name}/databases/database.db";

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public static boolean importDatabase(String dbPath)
    {
        //TODO EMPTY out all the tables
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        boolean ret = false;
        if (newDb.exists())
        {
            try
            {
                FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
                ret = true;
            }
            catch(Exception e )
            {
                ret = false;
                //TODO catch correct exception and log
            }
        }
        return ret;
    }
}
