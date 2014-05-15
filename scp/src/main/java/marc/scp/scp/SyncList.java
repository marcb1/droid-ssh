package marc.scp.scp;

/**
 * Created by Marc on 5/10/14.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import marc.scp.databaseutils.*;

public class SyncList extends Activity
{
    ListView listView;
    FileSync selectedSync;
    public final static String SELECTED_ID = "com.whomarc.scp.ID";

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
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setupListView(listView);
    }

    private void setupListView(ListView lv)
    {
        final List<FileSync> fileSyncList = Database.getInstance().getAllFileSync();
        List<String> titles = new ArrayList<String>();
        for (FileSync fs : fileSyncList)
        {
            if(fs != null)
                titles.add(fs.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSync = fileSyncList.get(position);
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
                intent.putExtra(SELECTED_ID, selectedSync.getId());
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
                new AlertDialog.Builder(activity).setMessage("Are you sure you would like to delete host: " + selectedSync.getName()+ "'?")
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
                                deleteSync(selectedSync);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void deleteSync(FileSync file)
    {
        Database.getInstance().deleteSync(file);
        finish();
    }
}

