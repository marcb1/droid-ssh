package marc.scp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

import marc.scp.asyncDialogs.Dialogs;
import marc.scp.asyncDialogs.YesNoDialog;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.HostKeys;
import marc.scp.viewPopulator.ListViews;
import marc.scp.scp.R;

/**
 * Created by Marc on 5/14/14.
 */

public class FingerPrintList extends Activity
{
    private ListView listView;
    private Dialogs DialogsInstance;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DialogsInstance = DialogsInstance.getInstance(this);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.fingerprint_list, null);
        listView = (ListView) contentView.findViewById(R.id.list_view);

        Button btnDeleteList = (Button) contentView.findViewById(R.id.button_delete);
        setupDeleteButton(btnDeleteList);
        setContentView(contentView);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setupListView(listView);
    }

    private void setupListView(ListView lv)
    {
        List<HostKeys> hostList = Database.getInstance().getAllHostKeys();
        SimpleAdapter adapter = ListViews.createAdapterFromFingerPrintList(this, hostList);
        lv.setAdapter(adapter);

    }

    private void setupDeleteButton (Button btnDeleteList)
    {
        final Activity activity = this;
        btnDeleteList.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                DialogsInstance.getConfirmDialog(activity, "Are you sure you would like to all saved Fingerprints?", true,
                        new YesNoDialog() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                deleteAll();
                                finish();
                                startActivity(getIntent());
                            }
                        }
                );
            }
        });
    }

    public void deleteAll()
    {
        Database.getInstance().deleteAllHostKeys();
    }
}