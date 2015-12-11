package marc.scp.threads;

import java.util.ArrayList;

import marc.scp.constants.Constants;
import marc.scp.activities.SyncActivity;
import marc.scp.databaseutils.FileSync;

/**
 * Created by Marc on 5/21/2014.
 */
public class SyncThread extends Thread
{
    private SyncActivity        _activity;
    private boolean             _stop;
    private ArrayList<FileSync> _filesList;

    private final static String LOG = Constants.LOG_PREFIX + "SyncThread";

    public SyncThread(SyncActivity parent, ArrayList<FileSync>  files)
    {
        _activity = parent;
        _stop = false;
        _filesList = files;
    }

    @Override
    public void run()
    {
        try
        {
            for(FileSync file: _filesList)
            {
                synchronized (_activity)
                {
                    if(_stop)
                    {
                        return;
                    }
                    // this is an async call which will spawn another thread to connect
                    _activity.sync(file);
                    _activity.wait();
                }
            }
        }
        catch (InterruptedException e)
        {
            android.util.Log.e(LOG, "caught exception while running sync thread", e);
        }
    }

    public void setStop ()
    {
        _stop = true;
    }

}
