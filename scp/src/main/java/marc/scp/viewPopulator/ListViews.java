package marc.scp.viewPopulator;

import android.app.Activity;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.databaseutils.HostKeys;
import marc.scp.databaseutils.Preference;

/**
 * Created by Marc on 5/16/14.
 */
public class ListViews
{
    public static SimpleAdapter createAdapterFromPrefs(Activity activity, List<Preference> preferencesList)
    {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for (Preference pr : preferencesList)
        {
            Map<String, String> datum = new HashMap<String, String>(2);
            if(pr != null)
                datum.put("host", pr.getName());
            List<HostKeys> keyList = Database.getInstance().getHostKey(pr.getHostName());

            System.out.println(keyList.size());
            if(keyList.size() == 0)
            {
                datum.put("fingerprint", "no fingerprint found");
            }
            else
            {
                datum.put("fingerprint", keyList.get(0).getFingerprint());
            }
            data.add(datum);

        }
        SimpleAdapter adapter = new SimpleAdapter(activity, data, android.R.layout.simple_list_item_2,
                new String[] {"host", "fingerprint"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        return adapter;
    }

    public static SimpleAdapter createAdapterFromFilePairs(Activity activity, List<FileSync> fileList)
    {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        if(fileList == null)
        {
            return null;
        }
        for (FileSync fs : fileList)
        {
            Map<String, String> datum = new HashMap<String, String>(2);
            if(fs != null)
            {
                    datum.put("name", fs.getName());
                    datum.put("folderlist", fs.getLocalFolder() + " > " + fs.getRemoteFolder());
            }
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(activity, data, android.R.layout.simple_list_item_2,
                new String[] {"name", "folderlist"},
                new int[] {android.R.id.text1, android.R.id.text2});
        return adapter;
    }
}
