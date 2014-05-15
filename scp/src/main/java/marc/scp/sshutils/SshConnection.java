package marc.scp.sshutils;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Properties;
import jackpal.androidterm.emulatorview.TermSession;

import java.io.File;

import android.util.Log;
import android.widget.TextView;

public class SshConnection extends TermSession
{
    //jsch objects
    private Session session;
    private Channel channel;
    private JSch jsch;

    private PipedInputStream localIn;
    private PipedOutputStream localOut;

    private SessionUserInfo userInfo;

    private final String log = "SshConnection";

    private enum CONNECTION_STATE
    {
        CONNECTED,
        CONNECTING,
        DISCONNECTED;
    }
    private CONNECTION_STATE state;


    public SshConnection(SessionUserInfo user, PipedInputStream i, PipedOutputStream o)
    {
        jsch = new JSch();
        userInfo = user;
        state = CONNECTION_STATE.DISCONNECTED;

        localIn = i;
        localOut = o;

        session = null;
        channel = null;
        //give user object, alerts user if they want to reconnect
        user.setConnectionHandler(this);

        try
        {
            PipedInputStream in = new PipedInputStream(localOut);
            PipedOutputStream out = new PipedOutputStream(localIn);
            setTermIn(in);
            setTermOut(out);

            session = jsch.getSession(userInfo.getUser(), userInfo.getHost(), userInfo.getPort());
            session.setHostKeyRepository(new FingerPrintRepository(jsch));
            session.setServerAliveInterval(10000);

            //enableCompression("9");

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

    public boolean connectAsShell()
    {
        boolean ret = false;
        try
        {
            if((session != null) && (state == CONNECTION_STATE.DISCONNECTED))
            {
                Log.d(log, "SSH Connecting...");
                state = CONNECTION_STATE.CONNECTING;
                session.connect(5000);

                channel = session.openChannel("shell");
                state = CONNECTION_STATE.CONNECTED;

                channel.setInputStream(localIn, true);
                channel.setOutputStream(localOut, true);

                channel.connect(5000);

                Log.d(log, "SSH Connected");
                ret = true;
            }
        }
        catch(JSchException  e)
        {
            Log.d(log, "Exception caught while initiating SSH connection: " + e.getMessage(), e);
            userInfo.handleException(e);
            ret = false;
            state = CONNECTION_STATE.DISCONNECTED;
        }
        return ret;
    }

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

    public void disconnect()
    {
        if(state != CONNECTION_STATE.DISCONNECTED)
        {
            if(channel != null)
            {
                channel.disconnect();
            }
            session.disconnect();
        }
        finish();
    }

    public boolean isConnected()
    {
        return state != CONNECTION_STATE.DISCONNECTED;
    }

    public String getName()
    {
        return userInfo.getHost();
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

                ((ChannelExec)channel).setCommand(command);


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
            channel.disconnect();
            return ret;
    }

    //not tested, need to be worked on
    boolean sendFile(File[] files, SftpProgressMonitor monitor)
    {
        boolean ret = false;
        if(true)
        {
            return ret;
        }
        try
        {
            //open channel ready to send input
            channel = session.openChannel("sftp");
            channel.setInputStream(null);

            channel.connect();
            ChannelSftp sftp = (ChannelSftp)channel;
            for (File file : files) {

                try
                {
                    sftp.put(file.getPath(), file.getName(), monitor, ChannelSftp.APPEND);
                }
                catch (SftpException e)
                {
                    e.printStackTrace();
                }
                ret = true;
            }
        }
        catch(Exception e)
        {

        }
        channel.disconnect();
        return ret;
    }

    @Override //called when data is processed from the input stream
    public void processInput(byte[] buffer, int offset, int count)
    {
        super.processInput(buffer, offset, count);
    }

 }

