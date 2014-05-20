package marc.scp.sshutils;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.File;
import java.util.List;
import java.util.Vector;

import jackpal.androidterm.emulatorview.TermSession;

import android.util.Log;

public class SshConnection
{
    //jsch objects
    private Channel channel;
    private JSch jsch;
    private Session session;

    private SessionUserInfo userInfo;

    private final String log = "SshConnection";

    protected enum CONNECTION_STATE
    {
        CONNECTED,
        CONNECTING,
        DISCONNECTED;
    }
    protected CONNECTION_STATE state;


    public JSch getJsch()
    {
        return jsch;
    }

    public SshConnection(SessionUserInfo user)
    {
        jsch = new JSch();
        channel = null;
        userInfo = user;

        state = CONNECTION_STATE.DISCONNECTED;
        try
        {
            session = jsch.getSession(userInfo.getUser(), userInfo.getHost(), userInfo.getPort());
            session.setHostKeyRepository(new FingerPrintRepository(jsch));
            session.setServerAliveInterval(10000);

            if(user.usingRSA())
            {
                //load(alias, private, public, passphrase)
                KeyPair keyPair = KeyPair.load(jsch, user.getRsa(), null);
                jsch.addIdentity(user.getHost(), keyPair.forSSHAgent(), null, null);
            }
            session.setUserInfo(user);
        }
        catch(JSchException e)
        {
            userInfo.handleException(e);
            Log.d(log, "Exception caught while creating jsch session" + e.getMessage());
            session = null;
        }
        catch(Exception e)
        {
            Log.d(log, "Exception caught while creating jsch session" + e.getMessage());
            session = null;
        }
    }

    public boolean connect()
    {
        boolean ret = true;
        if((state == state.DISCONNECTED))
        {
            return ret;
        }
        return ret;
    }

    public void disconnect()
    {
        if(state != CONNECTION_STATE.DISCONNECTED)
        {
            if(channel != null)
            {
                channel.disconnect();
            }
            session.disconnect();
            state = CONNECTION_STATE.DISCONNECTED;
        }
    }

    //setters
    public void enableCompression(String level)
    {
        session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none");
        session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none");
        session.setConfig("compression_level", level);
        Log.d(log, "enableCompression, level: " + level);
    }

    public void disableHostChecking()
    {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        Log.d(log, "Host checking disabled");
        session.setConfig(config);
    }

    //getters
    public boolean isConnected()
    {
        return state != CONNECTION_STATE.DISCONNECTED;
    }

    public String getName()
    {
        return userInfo.getHost();
    }

    protected SessionUserInfo getUserInfo()
    {
        return userInfo;
    }

    protected Session getSession()
    {
        return session;
    }

    public Channel getChannel()
    {
        return channel;
    }
}

