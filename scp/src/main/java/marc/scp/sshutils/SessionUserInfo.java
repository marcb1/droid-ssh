package marc.scp.sshutils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Locale;

import com.jcraft.jsch.UIKeyboardInteractive;

import marc.scp.scp.BlockingOnUIRunnable;
import marc.scp.scp.BlockingOnUIRunnableListener;
import marc.scp.scp.MyAlertBox;
import marc.scp.scp.TerminalActivity;
import marc.scp.scp.MyAlertDialog;


public class SessionUserInfo implements UserInfo, UIKeyboardInteractive
{

    private String mPassword;
    private String mRsa;
    private final String mUser;
    private final String mHost;
    private final int mPort;

    private TerminalActivity parent;

    //TODO make this private
    public boolean alertBooleanResult;

    private SshConnection conn;

    public SessionUserInfo(String host, String user, int port, TerminalActivity ac)
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
        String error = new String("Unknown error");
        if (paramJSchException.getMessage().contains("reject HostKey")) {
            error = new String("You have rejected the server's fingerprint");
        }
        else if (paramJSchException.getMessage().contains("UnknownHostKey")) {
            error = new String("Error: Unknown Host Key");
        }
        else if (paramJSchException.getMessage().contains("HostKey has been changed")) {
            error = new String("Error: Host key has been changed");
        }
        else if (paramJSchException.getMessage().contains("Auth fail")) {
            error = new String("Error: authentication failed, check username/password");
        }
        else if (paramJSchException.getMessage().contains("Auth cancel")) {
            error = new String("Error: Authentication has been cancelled");
        }
        else if (paramJSchException.getMessage().contains("socket is not established")) {
            error = new String("Error: Unable to establish connection, check that you are connected");
        }
        else if (paramJSchException.getMessage().contains("Too many authentication")) {
            error = new String(paramJSchException.getMessage());
        }
        else if (paramJSchException.getMessage().contains("Connection refused")) {
            error = new String("Connection refused");
        }
        else if (paramJSchException.getMessage().contains("Unable to resolve host"))
        {
            error = new String("Unable to establish connection, retry?");
            if(conn != null)
            {
                MyAlertDialog alert = new MyAlertDialog(parent, error, this);
                BlockingOnUIRunnable actionRunnable = new BlockingOnUIRunnable(parent, alert);
                actionRunnable.startOnUiAndWait();
                if(alertBooleanResult)
                {
                    conn.connectAsShell();
                }
                else
                {
                    parent.finish();
                }
                return;
            }
            else
            {
                error = new String("Error: Unable to establish connection, check that you are connected");
            }
        }

        MyAlertBox alert = new MyAlertBox(parent, error, this);
        BlockingOnUIRunnable actionRunnable = new BlockingOnUIRunnable(parent, alert);
        actionRunnable.startOnUiAndWait();
    }

    //abstract methods
    public String getPassphrase()
    {
        //log here that we returned the password
        return mPassword;
    }

    public void showMessage(String message)
    {
        MyAlertBox alert = new MyAlertBox(parent, message, this);
        BlockingOnUIRunnable actionRunnable = new BlockingOnUIRunnable(parent, alert);
        actionRunnable.startOnUiAndWait();
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