package marc.scp.Threads;

import android.app.Activity;
import android.util.Log;

import com.j256.ormlite.android.AndroidLog;

import java.util.ArrayList;

import marc.scp.activities.SyncActivity;
import marc.scp.databaseutils.FileSync;

/**
 * Created by Marc on 5/21/2014.
 */
public class SyncThread extends Thread
{
    private SyncActivity activity;
    private boolean stop;
    private ArrayList<FileSync> filesList;

    private final static String log = "SyncThread";

    public SyncThread(SyncActivity parent, ArrayList<FileSync>  files)
    {
        activity = parent;
        stop = false;
        filesList = files;
    }

    @Override
    public void run()
    {
        try
        {
            for(FileSync file: filesList)
            {
                synchronized (activity)
                {
                    if(stop)
                    {
                        return;
                    }
                    //this is an async call which will spawn another thread to connect
                    activity.sync(file);
                    activity.wait();
                }
            }
        }
        catch (InterruptedException e)
        {
            android.util.Log.e(log, "run", e);
        }
    }

    public void setStop()
    {
        stop = true;
    }

}
