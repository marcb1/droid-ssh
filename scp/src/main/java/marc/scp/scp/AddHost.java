package marc.scp.scp;

/**
 * Created by Marc on 5/13/14.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import android.widget.ToggleButton;




import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.Preference;

public class AddHost extends Activity
{
    private ViewGroup contentView;
    boolean usingRSAKey;
    Preference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        usingRSAKey = false;
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.add_host, null);
        pref = null;

        Bundle bundle = getIntent().getExtras();

        //if not null, get ID from database, and populate the text fields
        if ((bundle != null) && (bundle.containsKey(HostList.SELECTED_ID)))
        {
            EditText edit;
            int preferenceId = bundle.getInt(HostList.SELECTED_ID);
            pref = Database.getInstance().getPreferenceID(preferenceId);
            edit = (EditText) contentView.findViewById(R.id.hostNameField);
            edit.setText(pref.getHostName());
            edit = (EditText) contentView.findViewById(R.id.usernameField);
            edit.setText(pref.getUsername());
            edit = (EditText) contentView.findViewById(R.id.connectionNameField);
            edit.setText(pref.getName());

            edit = (EditText) contentView.findViewById(R.id.passwordField);
            System.out.println(pref.getRsaKey());
            System.out.println(pref.getPassword());
            if(pref.getUseKey())
            {
                Switch rsaToggle = (Switch) contentView.findViewById(R.id.RSAToggle);
                rsaToggle.toggle();
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
                edit.setText(pref.getRsaKey());
            }
            else
            {
                edit.setText(pref.getPassword());
            }

            System.out.println(pref.getPassword());
            edit = (EditText) contentView.findViewById(R.id.portField);
            edit.setText(String.valueOf(pref.getPort()));
        }

        Button btn = (Button) contentView.findViewById(R.id.button_save);
        setupAddandEditButton(btn);

        setContentView(contentView);
    }

    public void onToggleRSA(View view)
    {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();
        EditText edit = (EditText) contentView.findViewById(R.id.passwordField);
        if (on)
        {
            edit.setText("");
            usingRSAKey = true;
            edit.setHint("Enter location to RSA Key");
            edit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            usingRSAKey = false;
            edit.setText("");
            edit.setHint("Enter password");
            edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void setupAddandEditButton(Button btn)
    {
        btn.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {

                EditText edit;

                edit = (EditText) contentView.findViewById(R.id.connectionNameField);
                String name = edit.getText().toString();
                edit = (EditText) contentView.findViewById(R.id.hostNameField);
                String host = edit.getText().toString();
                edit = (EditText) contentView.findViewById(R.id.usernameField);
                String userName = edit.getText().toString();
                edit = (EditText) findViewById(R.id.portField);
                int port = Integer.parseInt(edit.getText().toString());
                Preference addP = new Preference(name, host, userName, port);

                edit = (EditText) contentView.findViewById(R.id.passwordField);
                String passwordOrKey = edit.getText().toString();
                if(!usingRSAKey)
                {
                    addP.setPassword(passwordOrKey);
                }
                else
                {
                    addP.setRsaKey(passwordOrKey);
                }

                if(pref == null)
                {
                    createNewPreference(addP);
                }
                else
                {
                    addP.setId(pref.getId());
                    updatePreference(addP);
                }
                backToHostList();
                finish();
            }
        });
    }

    private void backToHostList()
    {
        Intent intent = new Intent (this, HostList.class);
        startActivity(intent);
    }

    private void createNewPreference(Preference pref)
    {
        Database.getInstance().addPreference(pref);
    }

    private void updatePreference(Preference pref)
    {
        Database.getInstance().updatePreference(pref);
    }
}
