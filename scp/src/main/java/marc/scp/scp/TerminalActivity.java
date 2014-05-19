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
import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MenuInflater;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TerminalActivity extends Activity implements IConnectionNotifier
{
    private SshConnection conn;
    private int textSize;
    private boolean keyboardShown;

    private SharedPreferencesManager prefInstance;

    private TerminalSession terminalSession;
    private TerminalView view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_activity);

        prefInstance = SharedPreferencesManager.getInstance(this);
        keyboardShown = true;
        view  = (TerminalView) findViewById(R.id.emulatorView);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        view.setDensity(metrics);

        Button Ctrl = (Button) findViewById(R.id.Ctrl);
        setupControlButton(Ctrl);

        ImageButton leftButton = (ImageButton) findViewById(R.id.leftButton);
        setupLeftButton(leftButton);

        ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
        setupRightButton(rightButton);

        ImageButton keyboardButton = (ImageButton) findViewById(R.id.keyboardButton);
        setupKeyboardButton(keyboardButton);


        Intent intent = getIntent();
        Preference p = (Preference)intent.getParcelableExtra(MainActivity.PREFERENCE_PARCEABLE);

        SessionUserInfo user = new SessionUserInfo(p.getHostName(), p.getUsername(), p.getPort(), this);
        if(p.getUseKey())
        {
            user.setRSA(p.getRsaKey());
        }
        else
        {
            user.setPassword(p.getPassword());
        }

        ShellConnection connection = new ShellConnection(user);
        terminalSession = new TerminalSession(connection);

        conn = connection;
        textSize = Integer.parseInt(prefInstance.fontSize());

        //give the connection to the view so he can update Pty size on the fly
        view.addConnection(connection);

        view.attachSession(terminalSession);
        view.setTextSize(textSize);
        view.setAltSendsEsc(false);
        view.setMouseTracking(true);

        //view.setUseCookedIME(true);

        prefInstance.setPreferencesonShellConnection(conn);
        prefInstance.setPreferencesTerminal(view);

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
        }
        else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN))
        {
            textSize--;
            EmulatorView view = (EmulatorView) findViewById(R.id.emulatorView);
            view.setTextSize(textSize);
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

    private void setupControlButton(Button ctrl)
    {
        final View terminalView = view;
        ctrl.setOnClickListener(new View.OnClickListener()
        {
                    @Override
                    public void onClick(View v)
                    {
                        view.sendControlKey();
                    }
        });
    }

    private void setupLeftButton(ImageButton leftButton)
    {
        final View terminalView = view;
        leftButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                terminalSession.write("\033[D");
            }
        });
    }

    private void setupRightButton(ImageButton rightButton)
    {
        final View terminalView = view;
        rightButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                terminalSession.write("\033[C");
            }
        });
    }

    private void setupKeyboardButton(final ImageButton keyboard)
    {
        keyboard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleKeyboard();
            }
        });
    }

    private void handleKeyboard()
    {
        final InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        if(keyboardShown)
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            keyboardShown = false;
        }
        else
        {
            imm.showSoftInput(view, 0);
            keyboardShown = true;
        }
        view.updateSize(true);
        view.refreshScreen();
    }
}



