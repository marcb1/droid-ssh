package marc.scp.databaseutils;

/**
 * Created by Marc on 5/10/14.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Preference
{
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String connectionName;

    @DatabaseField
    private String hostName;

    @DatabaseField
    private int portNumber;

    @DatabaseField
    private String username;

    @DatabaseField
    private String password;

    @DatabaseField
    private String rsaKey;

    public Preference()
    {
    }

    public Preference(String name, String host, String user, int port)
    {
        connectionName = name;
        hostName = host;
        username = user;
        portNumber = port;
        rsaKey = null;
        password = null;
    }

    //setters
    public void setId(int id)
    {
        this.id = id;
    }

    public void setPassword(String p)
    {
        password = p;
        rsaKey = "";
    }

    public void setRsaKey(String r)
    {
        rsaKey = r;
        password = "";
    }

    //getters
    public int getId()
    {
        return id;
    }

    public boolean getUseKey()
    {
       boolean ret = false;
       if(rsaKey.length() > 2)
       {
           ret = true;
       }
        return ret;
    }

    public String getName()
    {
        return connectionName;
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

    public String getRsaKey()
    {
        return rsaKey;
    }
}