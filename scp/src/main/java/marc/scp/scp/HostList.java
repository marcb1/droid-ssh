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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import marc.scp.databaseutils.*;
import marc.scp.views.ListViews;

public class HostList extends Activity
{
    ListView listView;
    Preference selectedPref;
    public final static String SELECTED_ID = "com.whomarc.scp.ID";
    View oldview;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.host_list, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);

        Button btnAdd = (Button) contentView.findViewById(R.id.button_add);
        setupAddButton(btnAdd);

        Button btnEditList = (Button) contentView.findViewById(R.id.button_edit);
        setupEditButton(btnEditList);

        Button btnDeleteList = (Button) contentView.findViewById(R.id.button_delete);
        setupDeleteButton(btnDeleteList);

        setContentView(contentView);
        oldview = null;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setupListView(listView);
    }

    private void setupListView(ListView lv)
    {
        final List<Preference> preferencesList = Database.getInstance().getAllPreferences();
        lv.setAdapter(ListViews.createAdapterFromPrefs(this, preferencesList));

        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPref = preferencesList.get(position);
                view.setBackgroundColor(Color.GRAY);
                if(oldview != null)
                {
                    oldview.setBackgroundColor(Color.BLACK);
                }
                if(oldview != view)
                oldview = view;
            }
        });
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
                intent.putExtra(SELECTED_ID, selectedPref.getId());
                startActivity(intent);
            }
        });
    }

    private void setupDeleteButton (Button btnDeleteList)
    {
        final Activity activity = this;
        btnDeleteList.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v) {
                new AlertDialog.Builder(activity).setMessage("Are you sure you would like to delete host: " + selectedPref.getName()+ "'?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                deletePreference(selectedPref);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void deletePreference(Preference pref)
    {
        Database.getInstance().deletePreference(pref);
    }
}

