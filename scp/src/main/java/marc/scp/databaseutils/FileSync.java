package marc.scp.databaseutils;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Marc on 5/13/14.
 */
public class FileSync
{
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private int preferencesId;

    @DatabaseField
    private String localFolder;

    @DatabaseField
    private String remoteFolder;

    @DatabaseField
    private String syncName;


    public FileSync(String name, int pref, String local, String remote)
    {
        preferencesId = pref;
        localFolder = local;
        remoteFolder = remote;
        syncName = name;
    }

    public FileSync()
    {
    }

    //getters
    public String getName()
    {
        return syncName;
    }

    public String getRemoteFolder()
    {
        return remoteFolder;
    }

    public String getLocalFolder()
    {
        return localFolder;
    }

    public int getPreferencesId()
    {
        return preferencesId;
    }

    public int getId()
    {
        return id;
    }
}
