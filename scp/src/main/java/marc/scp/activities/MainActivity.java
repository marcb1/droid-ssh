package marc.scp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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

import com.jcraft.jsch.Cipher;
import com.jcraft.jsch.jce.AES256CTR;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import marc.scp.scp.R;
import marc.scp.constants.Constants;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.preferences.SharedPreferencesManager;
import marc.scp.viewPopulator.ListViews;
import marc.scp.databaseutils.Preference;
import marc.scp.asyncDialogs.Dialogs;


// This is the main activity; it is first started when the app is launched
public class MainActivity extends Activity
{
    static
    {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        Security.removeProvider("BC");
    }

    private ViewGroup   _contentView;
    private Dialogs     _dialogsInstance;
    private Database    _dbInstance;

    private final String LOG = Constants.LOG_PREFIX + "MainActivity";

    // this is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            Class clss = AES256CTR.class;
            Cipher c = (Cipher)(clss.newInstance());
            c.init(Cipher.ENCRYPT_MODE, new byte[c.getBlockSize()], new byte[c.getIVSize()]);
            javax.crypto.Cipher cipher=javax.crypto.Cipher.getInstance("AES/CTR/NoPadding");
            Log.d(LOG, "MARC works! " + cipher.getProvider());
        }
        catch(Exception e)
        {
            Log.d(LOG, "MARC no such provider probably");
        }

        Dialogs.init(this);
        _dialogsInstance = _dialogsInstance.getInstance();
        super.onCreate(savedInstanceState);
        _contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.main_activity, null);

        Database.init(this);
        SharedPreferencesManager.init(this);
        _dbInstance = Database.getInstance();
        SharedPreferencesManager.getInstance();

        Button quickConnect = (Button) _contentView.findViewById(R.id.quickConnect);
        setupQuickConnectBtn(quickConnect);

        Button syncAll = (Button) _contentView.findViewById(R.id.SyncAll);
        setupSyncAll(syncAll);

        setContentView(_contentView);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        ListView listView = (ListView) _contentView.findViewById(R.id.connections_list);
        setupConnectionsList(listView);

        listView = (ListView) _contentView.findViewById(R.id.folder_pair_list);
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
        final List<FileSync> fileList = _dbInstance.getAllFileSync();
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
        final List<Preference> preferencesList = _dbInstance.getAllPreferences();
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
                _dialogsInstance.getConfirmDialog(this, "Are you sure you want to exit?", true,
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
        syncAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<FileSync> fileList = new ArrayList<FileSync>(_dbInstance.getAllFileSync());
                if (fileList.size() >= 1) {
                    syncFiles(fileList);
                } else {
                    _dialogsInstance.makeToast(activity, "Please create a folder pair to sync!");
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
        EditText editText = (EditText) dialog.findViewById(R.id.connectField);
        // connectText = user@host:[optional_port]
        String connectText = editText.getText().toString();

        String[] userHost = connectText.split("@");
        String user = null;
        String host = null;

        int port = 22;
        if (userHost.length  > 1)
        {
            user = userHost[0];
            host = userHost[1];
        }
        else
        {
            // notify user
            _dialogsInstance.makeToast(this, "invalid format; must be: user@host");
        }

        String portHost[] = host.split(":");
        if (portHost.length > 1)
        {
            host = portHost[0];
            port = Integer.parseInt(portHost[1]);
        }

        if(host != null && user != null)
        {
            // create a new parceable preference object
            Preference pref = new Preference(host, host, user, port);
            connectToPreference(pref);
        }
    }
}