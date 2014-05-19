package marc.scp.sshutils;

import android.app.Activity;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;

import com.jcraft.jsch.UIKeyboardInteractive;

import marc.scp.syncDialogs.BlockingOnUIRunnable;
import marc.scp.syncDialogs.MyAlertBox;
import marc.scp.scp.TerminalActivity;
import marc.scp.syncDialogs.MyAlertDialog;


public class SessionUserInfo implements UserInfo, UIKeyboardInteractive
{

    private String mPassword;
    private String mRsa;
    private final String mUser;
    private final String mHost;
    private final int mPort;

    private Activity parent;

    //TODO make this private
    public boolean alertBooleanResult;

    private SshConnection conn;

    public SessionUserInfo(String host, String user, int port, Activity ac)
    {
        mHost = host;
        mUser = user;
        mPort = port;
        parent = ac;
        conn = null;
    }

    public String getRsa()
    {
        return mRsa;
    }
    public boolean usingRSA()
    {
        return (mPassword == null);
    }
    public void setPassword(String pass)
    {
        mPassword = pass;
        mRsa = null;
    }

    public void setRSA(String key)
    {
        mRsa = key;
        mPassword = null;
    }
    public int getPort()
    {
        return mPort;
    }

    public void setConnectionHandler(SshConnection c)
    {
        conn = c;
    }

    public String[] promptKeyboardInteractive(String destination, String name, String instruction,
                                              String[] prompt, boolean[] echo)
    {
        System.out.println("I GOT CALLED!");
        return null;
    }

    public void handleException(JSchException paramJSchException)
    {
        String error = new String(paramJSchException.getMessage());

        if (paramJSchException.getMessage().contains("reject HostKey")) {
            error = new String("You have rejected the server's fingerprint");
        }
        else if (paramJSchException.getMessage().contains("UnknownHostKey")) {
            error = new String("Unknown Host Key");
        }
        else if (paramJSchException.getMessage().contains("HostKey has been changed")) {
            error = new String("Host key has been changed");
        }
        else if (paramJSchException.getMessage().contains("Auth fail")) {
            error = new String("authentication failed, check username/password");
        }
        else if (paramJSchException.getMessage().contains("Auth cancel")) {
            error = new String("Authentication has been cancelled");
        }
        else if (paramJSchException.getMessage().contains("socket is not established")) {
            error = new String("Unable to establish connection, check the host's IP and your connection.");
        }
        else if (paramJSchException.getMessage().contains("Too many authentication")) {
            error = new String("Too many incorrect authentications with this user");
        }
        else if (paramJSchException.getMessage().contains("Connection refused")) {
            error = new String("Connection refused");
        }
        else if (paramJSchException.getMessage().contains("Unable to resolve host") || paramJSchException.getMessage().contains("Network is unreachable"))
        {
            error = new String("Unable to establish connection with the host, retry?");
            if(conn != null)
            {
                MyAlertDialog alert = new MyAlertDialog(parent, error, this);
                BlockingOnUIRunnable actionRunnable = new BlockingOnUIRunnable(parent, alert);
                actionRunnable.startOnUiAndWait();
                System.out.println("A");
                if(alertBooleanResult)
                {
                    conn.connect();
                }
                else
                {
                    parent.finish();
                }
                return;
            }
            else
            {
                error = new String("Unable to establish connection with the host.");
            }
        }
        showMessage("Error", error);
    }

    //abstract methods
    public String getPassphrase()
    {
        //log here that we returned the password
        return mPassword;
    }

    public void showMessage(String title, String message)
    {
        MyAlertBox alert = new MyAlertBox(parent, title, message, this);
        BlockingOnUIRunnable actionRunnable = new BlockingOnUIRunnable(parent, alert);
        actionRunnable.startOnUiAndWait();
    }

    @Override
    public void showMessage(String message)
    {
        showMessage("Info", message);
    }
    public boolean promptYesNo(final String message)
    {
        alertBooleanResult = false;

        MyAlertDialog alert = new MyAlertDialog(parent, message, this);
        BlockingOnUIRunnable actionRunnable = new BlockingOnUIRunnable(parent, alert);
        actionRunnable.startOnUiAndWait();

        return alertBooleanResult;
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