package marc.scp.sshutils;
import com.jcraft.jsch.UserInfo;

import java.io.InputStream;
import java.io.OutputStream;

public class SessionUserInfo implements UserInfo {

    private final String mPassword;
    private final String mUser;
    private final String mHost;
    private final int mPort;
    InputStream consoleIn;
    OutputStream consoleOut;

    public SessionUserInfo(String host, String user, String password, int port)
    {
        mHost = host;
        mUser = user;
        mPassword = password;
        mPort = port;
    }

    public void setConsole(InputStream i, OutputStream o)
    {
        consoleIn = i;
        consoleOut = o;
    }

    //abstract methods
    public String getPassphrase()
    {
        //log here that we returned the password
        return mPassword;
    }

    public void showMessage(java.lang.String arg0)
    {
        //TODO
    }
    public boolean promptYesNo(java.lang.String arg0)
    {
        // TODO
        return true;
    }
    public boolean promptPassword(java.lang.String arg0)
    {
        // TODO
        return true;
    }
    public boolean promptPassphrase(java.lang.String arg0)
    {
        // TODO
        return true;
    }

    public String getUser()
    {
        return mUser;
    }

    public String getHost()
    {
        return mHost;
    }

    public String getPassword()
    {
        return mPassword;
    }
}