package marc.scp.asyncNetworkTasks;

import android.os.AsyncTask;
import android.util.Log;

import marc.scp.scp.SyncActivity;
import marc.scp.sshutils.SshConnection;

/**
 * Created by Marc on 5/16/14.
 */
public class SftpConnectTask extends AsyncTask<SshConnection, Integer, Boolean>
{
    SyncActivity handler;
    SshConnection conn;

    private final String log = "SftpConnectTask";

    public SftpConnectTask(SyncActivity caller)
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
                ret = conn.connectAsSftp();
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