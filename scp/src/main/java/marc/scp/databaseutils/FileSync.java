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

    public FileSync(int pref, String local, String remote)
    {
        preferencesId = pref;
        localFolder = local;
        remoteFolder = remote;
    }

}
