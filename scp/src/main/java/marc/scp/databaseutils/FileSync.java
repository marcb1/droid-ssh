package marc.scp.databaseutils;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Marc on 5/13/14.
 */
public class FileSync implements Parcelable
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

    //setters
    public void setId(int id)
    {
        this.id = id;
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
        out.writeInt(preferencesId);
        out.writeString(localFolder);
        out.writeString(remoteFolder);
        out.writeString(syncName);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<FileSync> CREATOR = new Parcelable.Creator<FileSync>()
    {
        public FileSync createFromParcel(Parcel in)
        {
            return new FileSync(in);
        }

        public FileSync[] newArray(int size)
        {
            return new FileSync[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private FileSync(Parcel in)
    {
        id = in.readInt();
        preferencesId = in.readInt();
        localFolder = in.readString();
        remoteFolder = in.readString();
        syncName = in.readString();
    }
}
