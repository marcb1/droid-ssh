package marc.scp.sshutils;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UserInfo;
import com.jcraft.jsch.HostKeyRepository;

import java.util.List;

import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.HostKeys;
import marc.scp.databaseutils.Preference;
import java.util.Iterator;

/**
 * Created by Marc on 5/13/14.
 */
public class FingerPrintRepository implements HostKeyRepository
{
    private JSch parameter;
    Database dbInstance;

    FingerPrintRepository(JSch jsch)
    {
        parameter = jsch;
        dbInstance = Database.getInstance();
    }

    //if the host port is 22, host: domain, otherwise host: [domain]:6021
    public int check(String host, byte[] key)
    {
        try
        {
            List<HostKeys> keyList = dbInstance.getHostKey(host);
            if(keyList.size() > 0)
            {
                System.out.println(keyList.size());
                Iterator localIterator = keyList.iterator();
                while (localIterator.hasNext())
                {
                    HostKeys localHost = (HostKeys)localIterator.next();
                    HostKey JcHost = new HostKey(host, key);
                    boolean res = JcHost.getKey().equals(localHost.getKey());
                    System.out.println(localHost.getKey());
                    System.out.println(JcHost.getKey());
                    if(res == true)
                    {
                        System.out.println(res);
                        return 0;
                    }
                    else
                    {
                        return 2;
                    }
                }
            }
        }
        catch(Exception e)
        {
            return 1;
        }
        return 1;
    }

    public void add(HostKey hostkey, UserInfo ui)
    {
        HostKeys host = new HostKeys(hostkey.getHost(), hostkey.getFingerPrint(parameter), hostkey.getKey(), hostkey.getType());
        dbInstance.addHostKey(host);
    }

    public void remove(String host, String type) {
    }

    public void remove(String host, String type, byte[] key) {
    }

    public String getKnownHostsRepositoryID() {
        System.out.println("C");
        return null;
    }

    public HostKey[] getHostKey() {
        System.out.println("B");
        return null;
    }

    public HostKey[] getHostKey(String host, String type)
    {
        HostKey[] arrayOfHostKey = new HostKey[0];
        List<HostKeys> keyList = dbInstance.getHostKey(host);
        try
        {
            if(keyList.size() > 0)
            {
                Iterator localIterator = keyList.iterator();
                while (localIterator.hasNext())
                {
                    HostKeys localHost = (HostKeys)localIterator.next();
                    arrayOfHostKey[arrayOfHostKey.length] = new HostKey(localHost.getHostName(), localHost.getKey().getBytes());
                }
            }
        }
        catch(Exception e)
        {

        }
        return arrayOfHostKey;
    }
}