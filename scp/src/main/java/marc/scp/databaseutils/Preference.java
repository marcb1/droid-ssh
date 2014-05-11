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
    private String host;

    @DatabaseField
    private String password;


    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setUserName(String name) {
        username = name;
    }

    public String getName() {
        return host;
    }

    public void setHostName(String description) {
        host = description;
    }

    public String getHostName() {
        return host;
    }
}