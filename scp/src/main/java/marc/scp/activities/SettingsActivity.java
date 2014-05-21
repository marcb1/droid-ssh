package marc.scp.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import marc.scp.databaseutils.Database;

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
