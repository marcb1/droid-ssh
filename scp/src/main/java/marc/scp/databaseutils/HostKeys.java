package marc.scp.databaseutils;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Marc on 5/14/14.
 */
public class HostKeys
{
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String fingerprint;

    @DatabaseField
    private String key;

    @DatabaseField
    private String type;

    @DatabaseField
    private String hostName;

    public HostKeys()
    {

    }

    public HostKeys(String host, String fp, String k, String t)
    {
        fingerprint = fp;
        key = k;
        type = t;
        hostName = host;
    }

    public String getKey()
    {
        return key;
    }

    public String getFingerprint()
    {
        return fingerprint;
    }

    public String getType()
    {
        return type;
    }

    public String getHostName()
    {
        return hostName;
    }
}
