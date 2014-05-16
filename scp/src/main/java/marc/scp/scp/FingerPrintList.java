package marc.scp.scp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;

import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.HostKeys;

/**
 * Created by Marc on 5/14/14.
 */

public class FingerPrintList extends Activity
{
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        final List<HostKeys> hostList = Database.getInstance().getAllHostKeys();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for (HostKeys hk : hostList)
        {
            Map<String, String> datum = new HashMap<String, String>(2);
            if(hk != null)
            {
                datum.put("host", hk.getHostName());
                datum.put("fingerprint", hk.getFingerprint());
            }
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
        new String[] {"host", "fingerprint"},
        new int[] {android.R.id.text1, android.R.id.text2});
        lv.setAdapter(adapter);

    }

    private void setupDeleteButton (Button btnDeleteList)
    {
        final Activity activity = this;
        System.out.println("a");
        btnDeleteList.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                new AlertDialog.Builder(activity).setMessage("Are you sure you would like to all saved Fingerprints?")
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
                                deleteAll();
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public void deleteAll()
    {
        Database.getInstance().deleteAllHostKeys();
    }
}