package marc.scp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.preferences.SharedPreferencesManager;
import marc.scp.asyncDialogs.Dialogs;
import marc.scp.scp.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener
{
    private Dialogs Dialogs;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Dialogs = Dialogs.getInstance();
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
                    Dialogs.getConfirmDialog(getActivity(), "Are you sure you want to delete all saved data?", true,
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