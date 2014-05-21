package marc.scp.activities;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import marc.scp.scp.R;
import marc.scp.Constants.Constants;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.preferences.SharedPreferencesManager;
import marc.scp.viewPopulator.ListViews;
import marc.scp.databaseutils.Preference;
import marc.scp.asyncDialogs.Dialogs;

//this is the main activity that's first started when the app is launched
public class MainActivity extends Activity
{
    private ViewGroup contentView;
    private Dialogs DialogsInstance;

    private Database dbInstance;

    @Override
    //this is called when the activity is created
    protected void onCreate(Bundle savedInstanceState)
    {
        DialogsInstance = DialogsInstance.getInstance(this);
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.main_activity, null);

        Database.init(this);
        dbInstance = Database.getInstance();
        SharedPreferencesManager.getInstance(this);

        Button quickConnect = (Button) contentView.findViewById(R.id.quickConnect);
        setupQuickConnectBtn(quickConnect);

        Button syncAll = (Button) contentView.findViewById(R.id.SyncAll);
        setupSyncAll(syncAll);

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
        final List<FileSync> fileList = dbInstance.getAllFileSync();
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
                connectToPreference(p);
            }
        });
    }

    private void connectToPreference(Preference p)
    {
        Intent intent = new Intent(this, TerminalActivity.class);
        intent.putExtra(Constants.PREFERENCE_PARCEABLE, (Parcelable) p);
        startActivity(intent);
    }

    private void syncFile(FileSync f)
    {
        Intent intent = new Intent(this, SyncActivity.class);
        intent.putExtra(Constants.FILE_PARCEABLE, (Parcelable) f);
        startActivity(intent);
    }

    private void syncFiles(ArrayList<FileSync> fileList)
    {
        Intent intent = new Intent(this, SyncActivity.class);
        intent.putParcelableArrayListExtra(Constants.FILE_PARCEABLE, fileList);
        startActivity(intent);
    }

    private void ExitDialog()
    {
                DialogsInstance.getConfirmDialog(this, "Are you sure you want to exit?", true,
                        new YesNoDialog() {
                            @Override
                            public void PositiveMethod(final DialogInterface dialog, final int id) {
                                finish();
                            }
                        }
                );
    }

    private void setupSyncAll(Button syncAll)
    {
        final Activity activity = this;
        syncAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                ArrayList<FileSync> fileList = new ArrayList<FileSync>(dbInstance.getAllFileSync());
                if(fileList.size() >= 1)
                {
                    syncFiles(fileList);
                }
                else
                {
                    DialogsInstance.makeToast(activity, "Please create a folder pair to sync!");
                }
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
                dialog.setTitle("Quick Connect");
                dialogButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        connectToServer(dialog);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    // Called when the user clicks the quick connnect button
    public void connectToServer(Dialog dialog)
    {
        //grab the strings out of the correct field
        EditText editText = (EditText) dialog.findViewById(R.id.usernameField);
        String username = editText.getText().toString();

        editText = (EditText) dialog.findViewById(R.id.hostNameField);
        String hostname = editText.getText().toString();

        // create a new parceable preference object
        Preference pref = new Preference(hostname, hostname, username, 22);
        connectToPreference(pref);
    }
}