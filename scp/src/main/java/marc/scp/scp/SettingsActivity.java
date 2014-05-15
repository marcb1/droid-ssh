package marc.scp.scp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Marc on 5/15/14.
 */
public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
