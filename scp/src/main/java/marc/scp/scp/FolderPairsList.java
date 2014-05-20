package marc.scp.scp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import marc.scp.Constants.Constants;
import marc.scp.asyncDialogs.Dialogs;
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

    private View lastSelectedview;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.sync_list, null);

        listView = (ListView) contentView.findViewById(R.id.list_view);
        selectedFolderPair = null;

        Button btnAdd = (Button) contentView.findViewById(R.id.button_add);
        setupAddButton(btnAdd);

        Button btnEdt = (Button) contentView.findViewById(R.id.button_edit);
        setupEditButton(btnEdt);

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
                selectedFolderPair = fileList.get(position);
                if(lastSelectedview != null)
                {
                    lastSelectedview.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(getResources().getColor(R.color.list_selected));
                lastSelectedview = view;
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

    private void setupEditButton(Button btnEditList)
    {
        final Activity activity = this;
        btnEditList.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent (activity, AddFolderPair.class);
                if(selectedFolderPair != null)
                {
                    intent.putExtra(Constants.FILE_PARCEABLE, (Parcelable) selectedFolderPair);
                    startActivity(intent);
                }
                else
                {
                    Dialogs.makeToast(activity, "Please select a saved folder pair to edit!");
                }
            }
        });
    }
}
