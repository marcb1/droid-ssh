package marc.scp.scp;
import jackpal.androidterm.emulatorview.EmulatorView;
import marc.scp.sshutils.*;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.TextView;
import android.util.DisplayMetrics;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import jackpal.androidterm.emulatorview.TermSession;

public class TerminalActivity extends ActionBarActivity
{
    private EmulatorView emView;
    SshConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.USERNAME);
        String password = intent.getStringExtra(MainActivity.PASSWORD);
        String hostname = intent.getStringExtra(MainActivity.HOSTNAME);
        int port = Integer.parseInt(intent.getStringExtra(MainActivity.PORT));

        SessionUserInfo user = new SessionUserInfo(hostname, username, password, port);
        SshConnection connection = new SshConnection(user);
        conn = connection;

        //connection.disableHostChecking();
        SshConnectTask task = new SshConnectTask(this);
        task.execute(connection);
    }


    public void connectionResult(boolean result)
    {
        setContentView(R.layout.terminal_activity);

        //add buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EmulatorView view = (EmulatorView) findViewById(R.id.emulatorView);
        emView = view;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        view.setDensity(metrics);
        view.attachSession(conn);
        System.out.println("1");



        EditText mEntry = (EditText) findViewById(R.id.term_entry);
        mEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int action, KeyEvent ev) {
                // Ignore enter-key-up events
                if (ev != null && ev.getAction() == KeyEvent.ACTION_UP) {
                    return false;
                }
                // Don't try to send something if we're not connected yet

                Editable e = (Editable) v.getText();
                // Write to the terminal session
                System.out.println(e.toString());
                conn.write(e.toString());
                conn.write('\n');
                TextKeyListener.clear(e);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
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
        if (id == R.id.action_settings)
        {
            //openSaved();
            return true;
        }
        else if (id == R.id.action_saved)
        {
            //openSettings();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

}
