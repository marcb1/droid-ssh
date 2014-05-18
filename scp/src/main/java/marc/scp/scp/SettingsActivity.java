package marc.scp.scp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.preferences.SharedPreferencesManager;

/**
 * Created by Marc on 5/15/14.
 */
public class SettingsActivity extends PreferenceActivity
{
    private Database dbInstance;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        dbInstance = Database.getInstance();
    }
}
