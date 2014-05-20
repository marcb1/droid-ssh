package marc.scp.scp;

/**
 * Created by Marc on 5/10/14.
 */

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import marc.scp.Constants.Constants;
import marc.scp.asyncDialogs.Dialogs;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.*;
import marc.scp.viewPopulator.ListViews;

public class HostList extends Activity
{
    private ListView listView;
    private Preference selectedPref;
    private Dialogs Dialogs;

    private View lastSelectedview;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.host_list, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);
        Dialogs = Dialogs.getInstance(this);

        Button btnAdd = (Button) contentView.findViewById(R.id.button_add);
        setupAddButton(btnAdd);

        Button btnEditList = (Button) contentView.findViewById(R.id.button_edit);
        setupEditButton(btnEditList);

        Button btnDeleteList = (Button) contentView.findViewById(R.id.button_delete);
        setupDeleteButton(btnDeleteList);

        setContentView(contentView);
        selectedPref = null;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        selectedPref = null;
        setupListView(listView);
    }

    private void setupListView(final ListView lv)
    {
        final List<Preference> preferencesList = Database.getInstance().getAllPreferences();
        lv.setAdapter(ListViews.createAdapterFromPrefs(this, preferencesList));
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv.setOnItemClickListener(new OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectedPref = preferencesList.get(position);
                if(lastSelectedview != null)
                {
                    lastSelectedview.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(getResources().getColor(R.color.list_selected));
                lastSelectedview = view;
            }

        }
        );
    }


    private void setupAddButton(Button btnAdd)
    {
        final Activity activity = this;
        btnAdd.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent (activity, AddHost.class);
                startActivity(intent);
            }
        });
    }

    private void setupEditButton(Button btnEditList)
    {
        final Activity activity = this;
        btnEditList.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent (activity, AddHost.class);
                if(selectedPref != null)
                {
                    intent.putExtra(Constants.PREFERENCE_PARCEABLE, (Parcelable) selectedPref);
                    startActivity(intent);
                }
                else
                {
                    Dialogs.makeToast(activity, "Please select a saved connection to edit!");
                }
            }
        });
    }

    private void setupDeleteButton (Button btnDeleteList) {
        final Activity activity = this;
        final HostList hostlist = this;

        btnDeleteList.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v) {
                if(selectedPref != null) {
                    Dialogs.getConfirmDialog(activity, "Are you sure you would like to delete the connection: " + selectedPref.getName()
                                    + "? This will also delete the folder pairs associated with this connection.", true,
                            new YesNoDialog()
                            {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id)
                                {
                                    dialog.dismiss();
                                    hostlist.deletePreference(selectedPref);
                                }
                            }
                    );
                }
                else
                {
                    Dialogs.makeToast(activity, "Please select a saved connection to delete!");
                }
            }
    });
    }

    private void deletePreference(Preference pref)
    {
        Database.getInstance().deleteFileSyncsReferencingPref(pref.getId());
        Database.getInstance().deletePreference(pref);
        finish();
        startActivity(getIntent());
    }
}

