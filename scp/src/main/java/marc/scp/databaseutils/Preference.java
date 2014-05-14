package marc.scp.databaseutils;

/**
 * Created by Marc on 5/10/14.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Preference {
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String username;

    @DatabaseField
    private String hostName;

    @DatabaseField
    private String password;

    @DatabaseField
    private String rsaKey;

    @DatabaseField
    private String hostFingerPrint;

    @DatabaseField
    private int portNumber;

    public Preference()
    {

    }

    public Preference(String host, String user, int port)
    {
        portNumber = port;
        hostName = host;
        username = user;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPassword(String p)
    {
        password = p;
    }

    public void setRsaKey(String r)
    {
        rsaKey = r;
    }

    //getters
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return hostName;
    }

    public String getHostName()
    {
        return hostName;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public int getPort()
    {
        return portNumber;
    }

    public String getHostFingerPrint()
    {
        return hostFingerPrint;
    }

    public void setHostFingerPrint(String fingerPrint)
    {
        hostFingerPrint = fingerPrint;
    }
}