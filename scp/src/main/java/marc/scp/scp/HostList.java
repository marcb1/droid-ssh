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

        import marc.scp.databaseutils.*

public class HostList extends Activity
{
    ListView listView;
    Preference pref;

    private class Constants {
        public static final String keyWishListId = "wishListId";
        public static final String keyWishItemId = "wishItemId";
    }
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
        int host_id = getIntent().getExtras().getInt(Constants.keyWishListId);
        pref = Database.getInstance().getWishListWithId(wishListId);
        Log.i("WishList","wishList="+wishList+" wishListId="+wishListId);
        setupListView();
        setTitle("Wish list '"+wishList.getName()+"'");
    }

    private void setupListView() {
        if (null != pref) {
            final List<Preference> prefItems = pref.getItems();
            List<String> titles = new ArrayList<String>();
            for (pref wishItem : wishItems) {
                titles.add(wishItem.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
            listView.setAdapter(adapter);
            final Activity activity = this;
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    WishItem item = wishItems.get(position);
                    Intent intent = new Intent(activity,AddWishItemActivity.class);
                    intent.putExtra(Constants.keyWishItemId, item.getId());
                    startActivity(intent);
                }
            });
        }
    }


    private void setupAddButton(Button btnAdd)
    {
        final Activity activity = this;
        btnAdd.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v) {
                Intent intent = new Intent (activity,AddHost.class);
                intent.putExtra(Constants.keyWishListId, wishList.getId());
                startActivity(intent);
            }
        });
    }

    private void setupEditButton(Button btnEditList)
    {
        final Activity activity = this;
        btnEditList.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent (activity,AddWishListActivity.class);
                intent.putExtra(Constants.keyWishListId,wishList.getId());
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
                new AlertDialog.Builder(activity).setMessage("Are you sure you would like to delete list '"+ pref.getHostName()+"'?")
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
                                deletePreference();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void deletePreference() {
        Database.getInstance().deletePreference(pref);
        finish();
    }



}

