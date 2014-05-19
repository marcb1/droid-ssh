package marc.scp.asyncNetworkTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.util.List;

import marc.scp.sshutils.SftpConnection;
import marc.scp.sshutils.SshConnection;

/**
 * Created by Marc on 5/16/14.
 * Class to handle uploading of files
 */

public class SftpUploadTask extends AsyncTask<List<File>, Integer, Boolean>
{
    private SftpProgressMonitor sftpMonitor;
    private SftpConnection conn;
    private IUploadNotifier handler;

    private final String log = "SftpUploadTask";

    public SftpUploadTask(SftpProgressMonitor monitor, SftpConnection connection, IUploadNotifier callback)
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
            Log.e(log, "doInBackground exception", e);
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
