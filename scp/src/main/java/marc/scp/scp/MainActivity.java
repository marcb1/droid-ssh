package marc.scp.scp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.preferences.SharedPreferencesManager;
import marc.scp.viewPopulator.ListViews;
import marc.scp.databaseutils.Preference;

//this is the main activity that's first started when the app is launched
public class MainActivity extends Activity
{
    public final static String PREFERENCE_PARCEABLE = "com.whomarc.scp.PREFERENCE";
    public final static String FILE_PARCEABLE = "com.whomarc.scp.FILE";

    private ViewGroup contentView;

    private Database dbInstance;
    private SharedPreferencesManager prefInstance;

    @Override
    //this is called when the activity is created
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.main_activity, null);

        Database.init(this);
        dbInstance = Database.getInstance();
        prefInstance = SharedPreferencesManager.getInstance(this);

        Button quickConnect = (Button) contentView.findViewById(R.id.quickConnect);
        setupQuickConnectBtn(quickConnect);

        setContentView(contentView);
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

    @Override
    public void onBackPressed()
    {
        ExitDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        }
        else if(id == R.id.action_exit)
        {
            onBackPressed();
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
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

    private void setupQuickConnectBtn(Button quickConnect)
    {
        final Activity activity = this;
        quickConnect.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.quick_connect);
                Button dialogButton = (Button) dialog.findViewById(R.id.quickConnect);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    // TODO needs to be worked on
    // Called when the user clicks the quick connnect button
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


        // create a new parceable preference object

        startActivity(intent);
    }
}