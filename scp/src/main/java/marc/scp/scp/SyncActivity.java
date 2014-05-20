package marc.scp.scp;

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
import java.util.List;

import marc.scp.Constants.Constants;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.file_sync, null);
        setContentView(contentView);
        Dialogs = Dialogs.getInstance(this);

        dbInstance = Database.getInstance();

        Intent intent = getIntent();
        file = (FileSync)intent.getParcelableExtra(Constants.FILE_PARCEABLE);
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
        SharedPreferencesManager.getInstance(this).setPreferencesonShellConnection(conn);

        SshConnectTask task = new SshConnectTask(this);
        task.execute(conn);
    }


    @Override
    public void onBackPressed()
    {
        if(!conn.isConnected())
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
        switch (item.getItemId())
        {
            case android.R.id.home:
                exitDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            setTitle("Uploading...");
        }
        else
        {
            setTitle("Error...");
        }
    }

    //IUploadNotifier
    @Override
    public void transferResult(boolean result)
    {
        if(result)
        {
            setTitle("Done uploading.");
        }
        else
        {
            setTitle("Error...");
        }
        conn.disconnect();
        TextView t = (TextView) contentView.findViewById(R.id.fileUploadStatus);
        t.setText("");
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
        Dialogs.getConfirmDialog(this, "Are you sure you would like to disconnect?", true,
                new YesNoDialog()
                {
                    @Override
                    public void PositiveMethod(final DialogInterface dialog, final int id)
                    {
                        conn.disconnect();
                        finish();
                    }
                });
    }
}
