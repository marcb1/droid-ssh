package marc.scp.sshutils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.jcraft.jsch.UserInfo;

import java.io.InputStream;
import java.io.OutputStream;
import com.jcraft.jsch.UIKeyboardInteractive;

import marc.scp.scp.TerminalActivity;

public class SessionUserInfo implements UserInfo, UIKeyboardInteractive
{

    private final String mPassword;
    private final String mUser;
    private final String mHost;
    private final int mPort;

    TerminalActivity parent;

    public SessionUserInfo(String host, String user, String password, int port, TerminalActivity ac)
    {
        mHost = host;
        mUser = user;
        mPassword = password;
        mPort = port;
        parent = ac;
    }

    public int getPort()
    {
        return mPort;
    }

    public String[] promptKeyboardInteractive(String destination, String name, String instruction,
                                              String[] prompt, boolean[] echo)
    {
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
     //   final String m = message;
      //  new Thread()
      //  {
       //     public void run()
        //    {
         //       parent.prompt(m);
         //   }
       // }.start().join();

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