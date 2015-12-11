package marc.scp.viewPopulator;

import android.app.Activity;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.databaseutils.HostKeys;
import marc.scp.databaseutils.Preference;


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


    public static SimpleAdapter createAdapterFromFingerPrintList(Activity activity, final List<HostKeys> hostList)
    {
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
        SimpleAdapter adapter = new SimpleAdapter(activity, data, android.R.layout.simple_list_item_2,
                new String[] {"host", "fingerprint"},
                new int[] {android.R.id.text1, android.R.id.text2});
        return adapter;
    }

    //list files in a directory
    public static List<File> getListFiles(File parentDir)
    {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                inFiles.addAll(getListFiles(file));
            }
            else
            {
                inFiles.add(file);
            }
        }
        return inFiles;
    }
}
