package marc.scp.scp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.views.ListViews;
import marc.scp.databaseutils.Preference;

//this is the main activity that's first started when the app is launched
public class MainActivity extends Activity
{
    public final static String PREFERENCE_PARCEABLE = "com.whomarc.scp.PREFERENCE";
    public final static String FILE_PARCEABLE = "com.whomarc.scp.FILE";

    private ViewGroup contentView;

    private Database dbInstance;
    SharedPreferencesManager prefInstance;

    @Override
    //this is called when the activity is created
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.main_activity, null);

        Database.init(this);
        dbInstance = Database.getInstance();
        setContentView(contentView);
        prefInstance = SharedPreferencesManager.getInstance(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        ListView listView = (ListView) contentView.findViewById(R.id.connections_list);
        setupConnectionsList(listView);

        listView = (ListView) contentView.findViewById(R.id.folder_pair_list);
        setupFolderPairsList(listView);
    }

    private void setupFolderPairsList(ListView lv)
    {
        final List<FileSync> fileList = Database.getInstance().getAllFileSync();
        SimpleAdapter adapter = ListViews.createAdapterFromFilePairs(this, fileList);
        if((adapter != null))
        {
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FileSync f = fileList.get(position);
                    syncFile(f);
                }
            });

        }
    }

    private void setupConnectionsList(ListView lv)
    {
        final List<Preference> preferencesList = dbInstance.getAllPreferences();
        lv.setAdapter(ListViews.createAdapterFromPrefs(this, preferencesList));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Preference p = preferencesList.get(position);
                ConnectToPreference(p);
            }
        });
    }

    private void ConnectToPreference(Preference p)
    {
        Intent intent = new Intent(this, TerminalActivity.class);
        intent.putExtra(PREFERENCE_PARCEABLE, (Parcelable) p);
        startActivity(intent);
    }

    private void syncFile(FileSync f)
    {
        Intent intent = new Intent(this, SyncActivity.class);
        intent.putExtra(FILE_PARCEABLE, (Parcelable) f);
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
       /* LayoutInflater inflater = LayoutInflater.from(this);
        View dialogview = inflater.inflate(R.layout.dialoglayout, null);
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setTitle("Title");
        dialogbuilder.setView(dialogview);
        dialogDetails = dialogbuilder.create();*/

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


       // intent.putExtra(PASSWORD, password);
       // intent.putExtra(USERNAME, username);
       // intent.putExtra(HOSTNAME, hostname);
       // intent.putExtra(PORT, port);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_saved_hosts)
        {
            Intent intent = new Intent(this, HostList.class);
            startActivity(intent);
        }
        else if(id == R.id.action_saved_folders)
        {
            Intent intent = new Intent(this, FolderPairsList.class);
            startActivity(intent);
        }
        else if(id == R.id.action_cached_fingerprints)
        {
            Intent intent = new Intent(this, FingerPrintList.class);
            startActivity(intent);
        }
        else if(id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_exit)
        {
           onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        ExitDialog();
    }

    private void ExitDialog()
    {
                marc.scp.asyncDialogs.Dialogs.getConfirmDialog(this, "Are you sure you want to exit?", getString(R.string.yes), getString(R.string.no), true,
                new YesNoDialog()
                {
                    @Override
                    public void PositiveMethod(final DialogInterface dialog, final int id)
                    {
                        finish();
                    }
                });
    }
}