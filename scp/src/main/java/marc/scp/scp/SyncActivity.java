package marc.scp.scp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ProgressBar;

import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import jackpal.androidterm.emulatorview.EmulatorView;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.asyncTasks.FileUploadProgress;
import marc.scp.asyncTasks.IConnectionNotifier;
import marc.scp.asyncTasks.SftpConnectTask;
import marc.scp.asyncTasks.SftpUploadTask;
import marc.scp.asyncTasks.SshConnectTask;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.databaseutils.Preference;
import marc.scp.sshutils.SessionUserInfo;
import marc.scp.sshutils.SshConnection;

/**
 * Created by Marc on 5/16/14.
 */
public class SyncActivity  extends Activity implements IConnectionNotifier, SftpProgressMonitor
{
    private Database dbInstance;
    private SshConnection conn;
    private FileSync f;
    private ProgressBar mProgress;

    FileUploadProgress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbInstance = Database.getInstance();
        setContentView(R.layout.file_sync);

        Intent intent = getIntent();
        f = (FileSync)intent.getParcelableExtra(MainActivity.FILE_PARCEABLE);
        Preference p = dbInstance.getPreferenceID(f.getPreferencesId());

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

        SftpConnectTask task = new SftpConnectTask(this);
        progress = new FileUploadProgress();
        progress.doneUploading = false;
        task.execute(conn);
    }


    public void connectionResult(boolean result)
    {
        if(result)
        {
            setTitle("Connection Successful!");
            List<File> files = getListFiles(new File(f.getLocalFolder()));
              //  setTitle("uploading: " + file.getAbsoluteFile());
              //  System.out.println(file.getAbsoluteFile());
            SftpUploadTask task = new SftpUploadTask(this, conn);
            task.execute(files);
            waitOnEvents();
        }
        else
        {
            setTitle("Error...");
        }
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


    //SFTP Progress methods
    public boolean count(long count)
    {
        return true;
    }

    public void waitOnEvents()
    {
        synchronized (this)
        {
            try
            {
                if(!progress.doneUploading)
                {
                    this.wait();
                    setTitle("uploading: " + progress.src);
                }
            }
            catch(Exception e)
            {

            }
        }
    }

    public void init(int op, String src, String dest, long max)
    {
        synchronized (this)
        {
            if(!progress.doneUploading)
            {
                progress.src = src;
                progress.dst = dest;
                this.notify();
            }
        }
    }

    public void end()
    {
    }

    @Override
    public void onBackPressed()
    {
        System.out.println("onBackPressed");
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
                            finish();
                            conn.disconnect();
                            synchronized (activity)
                            {
                                progress.doneUploading = true;
                                activity.notifyAll();
                            }

                        }
                    });
        }
    }


}
