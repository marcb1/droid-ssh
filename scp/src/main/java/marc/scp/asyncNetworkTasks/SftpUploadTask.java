package marc.scp.asyncNetworkTasks;

import android.os.AsyncTask;

import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.util.List;

import marc.scp.sshutils.SshConnection;

/**
 * Created by Marc on 5/16/14.
 */

public class SftpUploadTask extends AsyncTask<List<File>, Integer, Boolean>
{
    SftpProgressMonitor sftpMonitor;
    SshConnection conn;
    IUploadNotifier handler;

    public SftpUploadTask(SftpProgressMonitor monitor, SshConnection connection, IUploadNotifier callback)
    {
        sftpMonitor = monitor;
        conn = connection;
        handler = callback;
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
        handler.transferResult(result);
    }
}
