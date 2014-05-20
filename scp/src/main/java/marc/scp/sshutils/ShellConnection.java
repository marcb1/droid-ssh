package marc.scp.sshutils;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;


/**
 * Created by Marc on 5/18/2014.
 * Inherits from SshConnection, instantiate this to create an shell connection through a terminal view
 */

public class ShellConnection extends SshConnection
{
    private PipedInputStream localIn;
    private PipedOutputStream localOut;

    private ChannelShell channelShell;

    private final String log = "ShellConnection";

    public ShellConnection(SessionUserInfo user)
    {
        super(user);

        localIn = new PipedInputStream();
        localOut = new PipedOutputStream();

        channelShell = null;

        //give user object, alerts user if they want to reconnect
        getUserInfo().setConnectionHandler(this);
    }

    public boolean connect()
    {
        boolean ret = false;
        try
        {
            Session session = super.getSession();
            if((session != null) && (state == CONNECTION_STATE.DISCONNECTED))
            {
                Log.d(log, "SSH shell Connecting...");
                Channel channel = super.getChannel();
                state = CONNECTION_STATE.CONNECTING;
                session.connect(5000);

                channel = session.openChannel("shell");
                channelShell = (ChannelShell)channel;

                channel.setInputStream(localIn, true);
                channel.setOutputStream(localOut, true);

                channel.connect(5000);
                state = CONNECTION_STATE.CONNECTED;

                Log.d(log, "SSH shell Connected");
                ret = true;
            }
        }
        catch(JSchException  e)
        {
            Log.e(log, "Exception caught while initiating shell connection", e);
            ret = false;
            state = CONNECTION_STATE.DISCONNECTED;
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
        if(channelShell != null)
        {
            channelShell.setPtySize(col, row, px, py);
        }
    }

    public void setPtyType(String type)
    {
        if(channelShell != null)
        {
            channelShell.setPtyType(type);
        }
    }

    public void setPty(boolean bool)
    {
        if(channelShell != null)
        {
            channelShell.setPty(bool);
        }
    }

    //This not supported anymore!
    public String executeCommand(String command)
    {
        String ret = null;
        if(state != CONNECTION_STATE.CONNECTED)
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
        return localIn;
    }

    public PipedOutputStream getOutputStream()
    {
        return localOut;
    }
}
