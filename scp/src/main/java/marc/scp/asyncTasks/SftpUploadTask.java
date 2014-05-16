package marc.scp.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.util.List;

import marc.scp.scp.SyncActivity;
import marc.scp.sshutils.SshConnection;

/**
 * Created by Marc on 5/16/14.
 */

public class SftpUploadTask extends AsyncTask<List<File>, Integer, Boolean>
{
    SftpProgressMonitor sftpMonitor;
    SshConnection conn;

    public SftpUploadTask(SftpProgressMonitor monitor, SshConnection connection)
    {
        sftpMonitor = monitor;
        conn = connection;
    }


    protected Boolean doInBackground(List<File>... files)
    {
        Boolean ret = false;
        try
        {
            if(conn.isConnected())
            {
                ret = conn.sendFiles(files[0], sftpMonitor);
            }
        }
        catch (Exception e)
        {
            //Log.d(log, "Exception caught while connectiong");
        }
        return ret;
    }

    protected void onProgressUpdate(String... progress)
    {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Boolean result)
    {
       // handler.connectionResult(result);
    }
}
