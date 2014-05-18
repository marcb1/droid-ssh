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

import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.preferences.SharedPreferencesManager;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(marc.scp.scp.R.xml.preferences);
        Preference p_delete = (Preference) this.findPreference(SharedPreferencesManager.DELETETABLES);
        p_delete.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick (Preference preference)
    {
        String key = preference.getKey();
        if(key.equals(SharedPreferencesManager.DELETETABLES))
        {
            marc.scp.asyncDialogs.Dialogs.getConfirmDialog(getActivity(), "Are you sure you want to delete all saved data?",
                    getString(R.string.yes), getString(R.string.no), true,
                    new YesNoDialog()
                    {
                        @Override
                        public void PositiveMethod(final DialogInterface dialog, final int id)
                        {
                            Database.getInstance().clearAllTables();
                        }
                    });
        }
        return true;
    }
}