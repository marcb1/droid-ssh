package marc.scp.sshutils;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;


/**
 * Inherits from SshConnection, instantiate this to create an shell connection through a terminal view
 */
public class ShellConnection extends SshConnection
{
    private PipedInputStream        _localIn;
    private PipedOutputStream       _localOut;

    private ChannelShell            _channelShell;

    private final String log = "DROID_SSH.ShellConnection";

    public ShellConnection(SessionUserInfo user)
    {
        super(user);

        _localIn = new PipedInputStream();
        _localOut = new PipedOutputStream();

        _channelShell = null;

        // give user object, alerts user if they want to reconnect
        getUserInfo().setConnectionHandler(this);
    }

    public boolean connect()
    {
        boolean ret = super.connect();
        if (ret == false)
        {
            Log.d(log, "failed to connect...");
            return ret;
        }
        try
        {
            Session session = super.getSession();
            if((session != null) && (_state == CONNECTION_STATE.DISCONNECTED))
            {
                Log.d(log, "SSH shell Connecting...");
                _state = CONNECTION_STATE.CONNECTING;
                session.connect(5000);

                _channel = session.openChannel("shell");

                _channel.setInputStream(_localIn, true);
                _channel.setOutputStream(_localOut, true);

                _channel.connect(5000);
                _channelShell = (ChannelShell)_channel;
                _state = CONNECTION_STATE.CONNECTED;

                Log.d(log, "SSH shell Connected");
                ret = true;
            }
        }
        catch(JSchException  e)
        {
            Log.e(log, "Exception caught while initiating shell connection", e);
            ret = false;
            _state = CONNECTION_STATE.DISCONNECTED;
            getUserInfo().handleException(e);
        }
        return ret;
    }

    @Override
    public void disconnect()
    {
        super.disconnect();
        //need to call finish on terminal session
    }

    public void setPtySize(int col, int row, int px, int py)
    {
        if(_channelShell != null)
        {
            Log.d(log, "setPtySize");
            _channelShell.setPtySize(col, row, px, py);
        }
    }

    public void setPtyType(String type)
    {
        if(_channelShell != null)
        {
            Log.d(log, "setPtyType: " + type);
            _channelShell.setEnv("TERM", type);
            _channelShell.setPtyType(type);
        }
    }

    public void setPty(boolean bool)
    {
        if(_channelShell != null)
        {
            _channelShell.setPty(bool);
        }
    }

    //This not supported anymore!
    public String executeCommand(String command)
    {
        String ret = null;
        if(_state != CONNECTION_STATE.CONNECTED)
        {
            return ret;
        }
        try
        {
            //open channel ready to send input

            ((ChannelExec)getChannel()).setCommand(command);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(new PipedInputStream()));

            while ((line = br.readLine()) != null)
            {
                stringBuilder.append(line + "\n");// append newline
            }

            //in.close();
            br.close();

            if (stringBuilder.length() > 0)
            {
                ret = stringBuilder.toString();
            }
            else
            {
                ret = "...\n";
            }
        }
        catch(Exception  e)
        {
            Log.d(log, e.getMessage());
        }
        disconnect();
        return ret;
    }

    public PipedInputStream getInputStream()
    {
        return _localIn;
    }

    public PipedOutputStream getOutputStream()
    {
        return _localOut;
    }
}
