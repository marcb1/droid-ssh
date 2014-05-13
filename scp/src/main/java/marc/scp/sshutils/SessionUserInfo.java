package marc.scp.sshutils;
import com.jcraft.jsch.UserInfo;

import java.io.InputStream;
import java.io.OutputStream;
import com.jcraft.jsch.UIKeyboardInteractive;

public class SessionUserInfo implements UserInfo, UIKeyboardInteractive {

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

    public String[] promptKeyboardInteractive(String destination, String name, String instruction,
                                              String[] prompt, boolean[] echo) {
        String str = new String(destination + ", " + name + ", " + instruction + "\n");
        byte[] s = str.getBytes();
        try {
            consoleOut.write(s, 0, s.length);
            for (String p : prompt)
            {
                s = p.getBytes();
                consoleOut.write(s, 0, s.length);
            }
        }
        catch (Exception e)
        {
        }
        return null;
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
    public boolean promptYesNo(String message)
    {
        byte[] s = message.getBytes();
        try {
            consoleOut.write(s, 0, s.length);
        }
        catch(Exception e)
        {

        }
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