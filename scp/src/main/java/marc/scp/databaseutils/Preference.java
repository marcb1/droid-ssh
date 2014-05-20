package marc.scp.databaseutils;

/**
 * Created by Marc on 5/10/14.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Preference implements Parcelable
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
       if(rsaKey.length() >= 1)
       {
           ret = true;
       }
        return ret;
    }

    public void setConnectionName(String name)
    {
        connectionName = name;
    }

    public void setHostName(String name)
    {
        hostName = name;
    }

    public void setPortNumber(int num)
    {
        portNumber = num;
    }

    public void setUsername(String user)
    {
        username = user;
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

    //parceable methods
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(id);
        out.writeString(connectionName);
        out.writeString(hostName);
        out.writeInt(portNumber);
        out.writeString(username);
        out.writeString(password);
        out.writeString(rsaKey);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Preference> CREATOR = new Parcelable.Creator<Preference>()
    {
        public Preference createFromParcel(Parcel in)
        {
            return new Preference(in);
        }

        public Preference[] newArray(int size)
        {
            return new Preference[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Preference(Parcel in)
    {
        id = in.readInt();
        connectionName = in.readString();
        hostName = in.readString();
        portNumber = in.readInt();
        username = in.readString();
        password = in.readString();
        rsaKey = in.readString();
    }
}