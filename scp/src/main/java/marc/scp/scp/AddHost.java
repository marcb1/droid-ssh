package marc.scp.scp;

/**
 * Created by Marc on 5/13/14.
 */
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.File;

import marc.scp.Constants.Constants;
import marc.scp.asyncDialogs.Dialogs;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.Preference;

public class AddHost extends Activity
{
    private ViewGroup contentView;
    private boolean usingRSAKey;
    private Preference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        usingRSAKey = false;
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.add_host, null);

        pref = (Preference)getIntent().getParcelableExtra(Constants.PREFERENCE_PARCEABLE);

        //if not null
        if (pref != null)
        {
            EditText edit;

            edit = (EditText) contentView.findViewById(R.id.hostNameField);
            edit.setText(pref.getHostName());
            edit = (EditText) contentView.findViewById(R.id.usernameField);
            edit.setText(pref.getUsername());
            edit = (EditText) contentView.findViewById(R.id.connectionNameField);
            edit.setText(pref.getName());

            edit = (EditText) contentView.findViewById(R.id.passwordField);

            if(pref.getUseKey())
            {
                Switch rsaToggle = (Switch) contentView.findViewById(R.id.RSAToggle);
                rsaToggle.toggle();
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
                edit.setText(pref.getRsaKey());
                usingRSAKey = true;
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

    private Preference createPreferenceObjectFromInput()
    {
        Preference p = null;
        EditText edit;
        final Activity activity = this;

        edit = (EditText) contentView.findViewById(R.id.connectionNameField);
        String name = edit.getText().toString();
        if(Dialogs.toastIfEmpty(name, activity, "You did not enter a connection name"))
        {
            return p;
        }
        edit = (EditText) contentView.findViewById(R.id.hostNameField);
        String host = edit.getText().toString();
        if(Dialogs.toastIfEmpty(host, activity, "You did not enter a host name"))
        {
            return p;
        }
        edit = (EditText) contentView.findViewById(R.id.usernameField);
        String userName = edit.getText().toString();
        if(Dialogs.toastIfEmpty(userName, activity, "You did not enter a user name"))
        {
            return p;
        }
        edit = (EditText) findViewById(R.id.portField);
        if(Dialogs.toastIfEmpty(edit.getText().toString(), activity, "You did not enter a port number"))
        {
            return p;
        }
        int port = Integer.parseInt(edit.getText().toString());

        edit = (EditText) contentView.findViewById(R.id.passwordField);
        String passwordOrKey = edit.getText().toString();
        if(!usingRSAKey)
        {
            if(Dialogs.toastIfEmpty(passwordOrKey, activity, "you did not enter a password"))
            {
                return p;
            }
            p  = new Preference(name, host, userName, port);
            p.setPassword(passwordOrKey);
        }
        else
        {
            if(Dialogs.toastIfEmpty(passwordOrKey, activity, "you did not enter an RSA key file"))
            {
                return p;
            }
            File file = new File(passwordOrKey);
            if(!file.exists())
            {
                Dialogs.makeToast(activity, "File does not exist");
                return p;
            }
            p  = new Preference(name, host, userName, port);
            p.setRsaKey(passwordOrKey);
        }
        return p;
    }

    private void setupAddandEditButton(Button btn)
    {
        final Activity  activity = this;
        btn.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                Preference addP = createPreferenceObjectFromInput();
                if(addP == null)
                {
                    return;
                }
                else if(pref == null)
                {
                    createNewPreference(addP);
                }
                else
                {
                    addP.setId(pref.getId());
                    updatePreference(addP);
                }
                finish();
            }
        });
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
