package marc.scp.sshutils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.jcraft.jsch.UserInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Locale;

import com.jcraft.jsch.UIKeyboardInteractive;

import marc.scp.scp.TerminalActivity;

public class SessionUserInfo implements UserInfo, UIKeyboardInteractive
{

    private final String mPassword;
    private final String mUser;
    private final String mHost;
    private final int mPort;

    TerminalActivity parent;

    PipedInputStream pin;
    PipedOutputStream pout;

    public SessionUserInfo(String host, String user, String password, int port, TerminalActivity ac, PipedInputStream in, PipedOutputStream out)
    {
        mHost = host;
        mUser = user;
        mPassword = password;
        mPort = port;
        parent = ac;
        pin = in;
        pout = out;
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
        String m = new String(message);

        try
        {
            byte[] data = m.getBytes("UTF-8");
            pout.write(data, 0, data.length);
            pout.flush();

            byte[] buffer = new byte[32];
            buffer[0] = (byte) pin.read();
            int cnt = Math.min(buffer.length-1, pin.available());
            pin.read(buffer, 1, cnt);

            String answer = new String(buffer, 0, 1+cnt);
            pout.write(buffer, 0, 1+cnt);
            boolean yes = (answer != null && answer.trim().toLowerCase(Locale.ENGLISH).startsWith("y"));
            return yes;
        } catch (Exception e) {
        }
        return false;
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