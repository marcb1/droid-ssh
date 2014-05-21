package marc.scp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marc.scp.threads.SyncThread;
import marc.scp.scp.R;
import marc.scp.constants.Constants;
import marc.scp.asyncDialogs.Dialogs;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.asyncNetworkTasks.IConnectionNotifier;
import marc.scp.asyncNetworkTasks.IUploadNotifier;
import marc.scp.asyncNetworkTasks.SftpUploadTask;
import marc.scp.asyncNetworkTasks.SshConnectTask;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.databaseutils.Preference;
import marc.scp.preferences.SharedPreferencesManager;
import marc.scp.sshutils.SessionUserInfo;
import marc.scp.sshutils.SftpConnection;
import marc.scp.viewPopulator.ListViews;


/**
 * Created by Marc on 5/16/14.
 */
public class SyncActivity  extends Activity implements IUploadNotifier, SftpProgressMonitor, IConnectionNotifier
{
    private Database dbInstance;

    private SftpConnection conn;
    private FileSync file;

    private ViewGroup contentView;
    private ProgressBar progress;
    private Dialogs Dialogs;

    private SyncThread syncThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.file_sync, null);
        setContentView(contentView);
        Dialogs = Dialogs.getInstance();

        dbInstance = Database.getInstance();

        Intent intent = getIntent();
        FileSync file = null;
        try
        {
            file = (FileSync) intent.getParcelableExtra(Constants.FILE_PARCEABLE);
        }
        catch(ClassCastException e)
        {
            file = null;
        }
        if (file != null)
        {
            sync(file);
        }
        else
        {
            final ArrayList<FileSync> filesList = this.getIntent().getParcelableArrayListExtra(Constants.FILE_PARCEABLE);
            if (filesList != null)
            {
                syncAll(filesList);
            }
        }
    }

    private void syncAll(final ArrayList<FileSync> filesList)
    {
        syncThread = new SyncThread(this, filesList);
        syncThread.start();
    }

    //public so SyncThread can call into here
    public void sync(FileSync file)
    {
        this.file = file;
        Preference p = dbInstance.getPreferenceID(file.getPreferencesId());

        SessionUserInfo user = new SessionUserInfo(p.getHostName(), p.getUsername(), p.getPort(), this);
        if(p.getUseKey())
        {
            user.setRSA(p.getRsaKey());
        }
        else
        {
            user.setPassword(p.getPassword());
        }

        conn = new SftpConnection(user);

        progress = (ProgressBar) contentView.findViewById(R.id.progressBar);
        SharedPreferencesManager.getInstance().setPreferencesonShellConnection(conn);

        SshConnectTask task = new SshConnectTask(this);
        task.execute(conn);
    }

    @Override
    public void onBackPressed()
    {
        if((!conn.isConnected()) && (syncThread == null))
        {
            super.onBackPressed();
        }
        else if((!conn.isConnected()) && (syncThread.getState() == Thread.State.TERMINATED))
        {
            super.onBackPressed();
        }
        else
        {
          exitDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            if((!conn.isConnected()) && (syncThread == null))
            {
                super.onOptionsItemSelected(item);
            }
            else if((!conn.isConnected()) && (syncThread.getState() == Thread.State.TERMINATED))
            {
                super.onOptionsItemSelected(item);
            }
            else
            {
                exitDialog();
            }
        }
        return true;
    }

    //IConnectionNotifier
    @Override
    public void connectionResult(boolean result)
    {
        if(result)
        {
            setTitle("Connected");

            File toSync = new File(file.getLocalFolder());
            if(!toSync.exists())
            {
                Dialogs.getAlertDialog(this, "Error", "Local folder: '" + file.getLocalFolder() + "' does not exist", true);
                conn.disconnect();
                return;
            }
            List<File> files = ListViews.getListFiles(toSync);
            boolean remoteExists = conn.changeRemoteDirectory(file.getRemoteFolder());
            if(!remoteExists)
            {
                Dialogs.getAlertDialog(this, "Error", "Remote folder: '" + file.getRemoteFolder() + "' does not exist", true);
                conn.disconnect();
                return;
            }
            SftpUploadTask task = new SftpUploadTask(this, conn, this);
            task.execute(files);
            setTitle("Uploading: " + file.getName());
        }
        else
        {
            setTitle("Error connecting...");
        }
    }

    //IUploadNotifier
    @Override
    public void transferResult(boolean result)
    {
        System.out.println(result);
        if(result)
        {
            setTitle("Done uploading.");
        }
        else
        {
            setTitle("Error transferring files...");
        }
        conn.disconnect();
        TextView t = (TextView) contentView.findViewById(R.id.fileUploadStatus);
        t.setText("");
        synchronized (this)
        {
            this.notify();
        }
    }

    //SFTP Progress methods
    @Override
    public boolean count(final long count)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                    progress.incrementProgressBy((int) count);
            }
        });

        return true;
    }

    @Override
    public void init(int op, final String src, final String dest, long m)
    {
        progress.setMax((int) m);
        this.runOnUiThread(new Runnable() {
            public void run() {
                TextView t = (TextView) contentView.findViewById(R.id.fileUploadStatus);
                t.setText("uploading: " + src + "...");
            }
        });
    }

    @Override
    public void end()
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                progress.setProgress(0);
            }
        });
    }

    private void exitDialog()
    {
        final Activity activity = this;
        final SyncThread thread = syncThread;
        Dialogs.getConfirmDialog(this, "Are you sure you would like to disconnect?", true,
                new YesNoDialog()
                {
                    @Override
                    public void PositiveMethod(final DialogInterface dialog, final int id)
                    {
                        try
                        {
                            synchronized (activity)
                            {
                                thread.setStop();
                                activity.notify();
                            }
                            thread.join();
                        }
                        catch(Exception e)
                        {

                        }
                        conn.disconnect();
                        finish();
                    }
                });
    }
}
