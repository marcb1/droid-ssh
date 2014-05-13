package marc.scp.scp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.DatabaseHelper;
import marc.scp.databaseutils.Preference;

//this is the main acitivity that's first started when the app is launched
public class MainActivity extends ActionBarActivity
{
    public final static String USERNAME = "com.whomarc.scp.USERNAME";
    public final static String PASSWORD = "com.whomarc.scp.PASSWORD";
    public final static String HOSTNAME = "com.whomarc.scp.HOSTNAME";
    public final static String PORT = "com.whomarc.scp.PORT";
    public final static String RSAKEY = "com.whomarc.scp.RSAKEY";

    ListView listView;
    Preference selectedPref;

    DatabaseHelper helper;

    @Override
    //this is called when the activity is created
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);
        Database.init(this);
        setContentView(contentView);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setupListView(listView);
    }

    private void setupListView(ListView lv)
    {
        final List<Preference> preferencesList = Database.getInstance().getAllPreferences();
        List<String> titles = new ArrayList<String>();
        for (Preference pr : preferencesList)
        {
            if(pr != null)
                titles.add("Host: " + pr.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Preference p = preferencesList.get(position);
                ConnectToPreference(p);
            }
        });
    }

    public void ConnectToPreference(Preference p)
    {
        Intent intent = new Intent(this, TerminalActivity.class);

        intent.putExtra(PASSWORD, p.getPassword());
        System.out.println("password" + p.getPassword());
        intent.putExtra(USERNAME, p.getUsername());
        System.out.println("user" + p.getUsername());
        intent.putExtra(HOSTNAME, p.getHostName());
        System.out.println("host" + p.getHostName());
        intent.putExtra(PORT, String.valueOf(p.getPort()));
        System.out.println("host" + String.valueOf(p.getPort()));

        startActivity(intent);

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
        if (id == R.id.action_saved)
        {
            Intent intent = new Intent(this, HostList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}