package marc.scp.scp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import marc.scp.asyncDialogs.Dialogs;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.databaseutils.HostKeys;
import marc.scp.databaseutils.Preference;

/**
 * Created by Marc on 5/15/14.
 */
public class AddFolderPair  extends Activity
{
    private ViewGroup contentView;
    Database dbInstance;
    int selectedItemSpinner;
    HashMap<String, Integer> hash;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.add_folder_pair, null);
        dbInstance = Database.getInstance();
        hash = new HashMap<String, Integer>();

        Button btn = (Button) contentView.findViewById(R.id.button_save);
        setupAddandEditButton(btn);

        Spinner spinner = (Spinner) contentView.findViewById(R.id.connection_list);
        populateSpinner(spinner);

        setContentView(contentView);
        selectedItemSpinner = -1;
    }

    private void populateSpinner(Spinner spinner)
    {
        final List<Preference> preferencesList = Database.getInstance().getAllPreferences();
        if(preferencesList == null)
        {
            //TODO unify alert dialog to one class and alert user of error
            return;
        }
        ArrayList<String> prefList = new ArrayList<String>();

        System.out.println(2);
        for (Preference pr: preferencesList)
        {
            if(pr != null)
            {
                System.out.println(2);
                prefList.add(pr.getName());
                hash.put(pr.getName(), pr.getId());
                System.out.println(4);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, prefList);
        spinner.setAdapter(adapter);
        System.out.println(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String host = (String)parent.getItemAtPosition(pos);
                selectedItemSpinner = hash.get(host);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
                String host = (String)parent.getItemAtPosition(0);
                selectedItemSpinner = hash.get(host);
            }
        });
    }

    private void setupAddandEditButton(Button btn)
    {
        final Activity activity = this;
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText edit;

                edit = (EditText) contentView.findViewById(R.id.folderPairName);
                String name = edit.getText().toString();
                if(Dialogs.toastIfEmpty(name, activity, "You did not enter a folder pair name."))
                {
                    return;
                }

                edit = (EditText) contentView.findViewById(R.id.localFolderField);
                String localFolder = edit.getText().toString();
                if(Dialogs.toastIfEmpty(localFolder, activity, "You did not enter a local folder."))
                {
                    return;
                }
                edit = (EditText) contentView.findViewById(R.id.remoteFolderField);
                String remoteFolder = edit.getText().toString();
                if(Dialogs.toastIfEmpty(remoteFolder, activity, "You did not enter a remote folder."))
                {
                    return;
                }

                if(selectedItemSpinner == -1)
                {
                    Dialogs.makeToast(activity, "You did not select a connection to sync associate this folder sync with.");
                    return;
                }
                FileSync file = new FileSync(name, selectedItemSpinner, localFolder, remoteFolder);
                dbInstance.addFileSync(file);
                finish();
            }
        });
    }
}
