package marc.scp.scp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.app.ActionBar;
import android.util.Log;

//this is the main acitivity that's first started when the app is launched
public class MainActivity extends ActionBarActivity
{
    public final static String USERNAME = "com.whomarc.scp.USERNAME";
    public final static String PASSWORD = "com.whomarc.scp.PASSWORD";
    public final static String HOSTNAME = "com.whomarc.scp.HOSTNAME";
    public final static String PORT = "com.whomarc.scp.PORT";
    public final static String RSAKEY = "com.whomarc.scp.RSAKEY";

    DatabaseHelper helper;

    @Override
    //this is called when the activity is created
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DatabaseHelper(this);

        //String test = "a";
        //helper.addToTable(test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called when the user clicks the connnect button
    public void connectToServer(View view)
    {
        //MainActivity inherits from context (this)
        Intent intent = new Intent(this, TerminalActivity.class);

        //grab the strings out of the correct field
        EditText editText = (EditText) findViewById(R.id.usernameField);
        String username = editText.getText().toString();

        editText = (EditText) findViewById(R.id.passwordField);
        String password = editText.getText().toString();

        editText = (EditText) findViewById(R.id.hostNameField);
        String hostname = editText.getText().toString();

        editText = (EditText) findViewById(R.id.portField);
        String port = editText.getText().toString();


        intent.putExtra(PASSWORD, password);
        intent.putExtra(USERNAME, username);
        intent.putExtra(HOSTNAME, hostname);
        intent.putExtra(PORT, port);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}