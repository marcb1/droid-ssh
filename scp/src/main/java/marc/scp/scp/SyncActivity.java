package marc.scp.scp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marc.scp.asyncDialogs.Dialogs;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.asyncNetworkTasks.IUploadNotifier;
import marc.scp.asyncNetworkTasks.SftpConnectTask;
import marc.scp.asyncNetworkTasks.SftpUploadTask;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.databaseutils.Preference;
import marc.scp.preferences.SharedPreferencesManager;
import marc.scp.sshutils.SessionUserInfo;
import marc.scp.sshutils.SshConnection;

/**
 * Created by Marc on 5/16/14.
 */
public class SyncActivity  extends Activity implements IUploadNotifier, SftpProgressMonitor
{
    private Database dbInstance;

    private SshConnection conn;
    private FileSync file;

    private ViewGroup contentView;

    private long max;
    private long lastResult;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbInstance = Database.getInstance();
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.file_sync, null);
        setContentView(contentView);

        Intent intent = getIntent();
        file = (FileSync)intent.getParcelableExtra(MainActivity.FILE_PARCEABLE);
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

        conn = new SshConnection(user);

        progress = (ProgressBar) contentView.findViewById(R.id.progressBar);
        SharedPreferencesManager.getInstance(this).setPreferencesonConnection(conn);

        SftpConnectTask task = new SftpConnectTask(this);
        task.execute(conn);
    }


    public void connectionResult(boolean result)
    {
        if(result)
        {
            setTitle("Connected");
            System.out.println("Connected");
            File toSync = new File(file.getLocalFolder());
            if(!toSync.exists())
            {
                Dialogs.getAlertDialog(this, "Error", "Local folder: '" + file.getLocalFolder() + "' does not exist", true);
                conn.disconnect();
                return;
            }
            List<File> files = getListFiles(toSync);
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
            System.out.println("Error");
            setTitle("Error...");
        }
    }

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

    private List<File> getListFiles(File parentDir)
    {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                inFiles.addAll(getListFiles(file));
            }
            else
            {
                inFiles.add(file);
            }
        }
        return inFiles;
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
            final Activity activity = this;
            marc.scp.asyncDialogs.Dialogs.getConfirmDialog(this, "Are you sure you would like to disconnect?", getString(R.string.yes), getString(R.string.no), true,
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
}
