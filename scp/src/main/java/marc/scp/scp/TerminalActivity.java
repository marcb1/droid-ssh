package marc.scp.scp;
import jackpal.androidterm.emulatorview.EmulatorView;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.asyncNetworkTasks.IConnectionNotifier;
import marc.scp.asyncNetworkTasks.SshConnectTask;
import marc.scp.databaseutils.Preference;
import marc.scp.preferences.SharedPreferencesManager;
import marc.scp.sshutils.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MenuInflater;
import android.util.DisplayMetrics;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TerminalActivity extends Activity implements IConnectionNotifier
{
    private SshConnection conn;
    private int textSize;
    SharedPreferencesManager prefInstance;
    TerminalView view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity);
        prefInstance = SharedPreferencesManager.getInstance(this);

       view  = (TerminalView) findViewById(R.id.emulatorView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        view.setDensity(metrics);

        Intent intent = getIntent();
        Preference p = (Preference)intent.getParcelableExtra(MainActivity.PREFERENCE_PARCEABLE);

        PipedInputStream in  = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();

        SessionUserInfo user = new SessionUserInfo(p.getHostName(), p.getUsername(), p.getPort(), this);
        if(p.getUseKey())
        {
            user.setRSA(p.getRsaKey());
        }
        else
        {
            user.setPassword(p.getPassword());
        }
        System.out.println(1);
        SshConnection connection = new SshConnection(user, in, out);

        conn = connection;
        textSize = Integer.parseInt(prefInstance.fontSize());
        view.addConnection(conn);
        view.attachSession(conn);
        view.setTextSize(textSize);
        view.setAltSendsEsc(false);
        view.setMouseTracking(true);

        System.out.println(2);

        view.setTermType("vt100");
        //view.setUseCookedIME(true);

        SharedPreferencesManager.getInstance(this).setPreferencesonConnection(conn);

        SshConnectTask task = new SshConnectTask(this);
        task.execute(conn);
    }

    public void connectionResult(boolean result)
    {
        if(result == false)
        {
            setTitle("Error...");
            return;
        }
        setTitle(conn.getName());
        conn.channelShell.setPtyType("xterm-256color");
        view.refreshScreen();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean ret = true;
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            textSize++;
            EmulatorView view = (EmulatorView) findViewById(R.id.emulatorView);
            view.setTextSize(textSize);

           // conn.updateSize(view.getWidth(), view.getHeight());
           // conn.reset();

            view.setSelected(true);
            view.updateSize(true);
            view.invalidate();
        }
        else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN))
        {
            textSize--;
            EmulatorView view = (EmulatorView) findViewById(R.id.emulatorView);
            view.setTextSize(textSize);

            //conn.updateSize(view.getWidth(), view.getHeight());
            view.updateSize(true);
            view.invalidate();
        }
        else
        {
            ret = super.onKeyDown(keyCode, event);
        }
        return ret;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean ret = true;
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
        }
        else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN))
        {
        }
        else
        {
            ret = super.onKeyUp(keyCode, event);
        }
        return ret;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.terminal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //when user clicks on action bar item
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            if(!conn.isConnected())
            {
                return super.onOptionsItemSelected(item);
            }
            new AlertDialog.Builder(this).setMessage("Are you sure you would like to disconnect?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            conn.disconnect();
                            finish();
                        }
                    })
                    .create()
                    .show();
            return true;
        }
        else if (id == R.id.action_settings)
        {
            //openSaved();
            return true;
        }
        //else if (//id == R.id.action_saved)
       // {
            //openSettings();
         //   return true;
       // }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        System.out.println("onBackPressed");
        if(!conn.isConnected())
        {
            try
            {
                //kill the connecting thread
                if(conn != null)
                {
                    conn.disconnect();
                }
                super.onBackPressed();
            }
            catch(Exception e)
            {

            }
        }
        else
        {
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

}



