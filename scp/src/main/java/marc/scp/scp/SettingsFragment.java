package marc.scp.scp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(marc.scp.scp.R.xml.preferences);

        Preference deleteKey = (Preference)findPreference("clear_history");
        setupDelete(deleteKey);
    }


    private void setupDelete(Preference del)
    {
    }
}