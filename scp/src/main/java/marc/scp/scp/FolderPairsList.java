package marc.scp.scp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.viewPopulator.ListViews;

/**
 * Created by Marc on 5/14/14.
 */
public class FolderPairsList extends Activity
{
    private ListView listView;
    private FileSync selectedFolderPair;
    public final static String SELECTED_ID = "com.whomarc.scp.ID";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.sync_list, null);

        listView = (ListView) contentView.findViewById(R.id.list_view);
        selectedFolderPair = null;

        Button btnAdd = (Button) contentView.findViewById(R.id.button_add);
        setupAddButton(btnAdd);

        setContentView(contentView);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setupListView(listView);
        selectedFolderPair = null;
    }

    private void setupListView(ListView lv)
    {
        final List<FileSync> fileList = Database.getInstance().getAllFileSync();
        lv.setAdapter(ListViews.createAdapterFromFilePairs(this, fileList));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(selectedFolderPair == fileList.get(position))
                {
                    view.setBackgroundColor(Color.BLACK);
                }
                else
                {
                    selectedFolderPair = fileList.get(position);
                    view.setBackgroundColor(Color.GRAY);
                }

            }
        });
    }

    private void setupAddButton(Button btnAdd)
    {
        final Activity activity = this;
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent (activity, AddFolderPair.class);
                startActivity(intent);
            }
        });
    }
}
