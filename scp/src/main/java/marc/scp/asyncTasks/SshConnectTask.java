package marc.scp.asyncTasks;
import marc.scp.scp.*;
import marc.scp.sshutils.SshConnection;


import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Marc on 5/1/14.
 */

public class SshConnectTask extends AsyncTask<SshConnection, Integer, Boolean>
{
    IConnectionNotifier handler;
    SshConnection conn;

    private final String log = "SshConnectTask";

    public SshConnectTask(IConnectionNotifier caller)
    {
        handler = caller;
        conn = null;
    }

    protected Boolean doInBackground(SshConnection... connection)
    {
        Boolean ret = false;
        try
        {
            conn = connection[0];
            if(!conn.isConnected())
            {
                ret = conn.connectAsShell();
            }
        }
        catch (Exception e)
        {
            Log.d(log, "Exception caught while connectiong");
        }
        return ret;
    }


    protected void onProgressUpdate(String... progress)
    {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Boolean result)
    {
        handler.connectionResult(result);
    }

}
