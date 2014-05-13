package marc.scp.sshutils;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;
import jackpal.androidterm.emulatorview.TermSession;

import java.io.File;

import android.util.Log;
import android.widget.TextView;

public class SshConnection  extends TermSession
{
    //jsch session
    private Session session;
    private Channel channel;
    TextView console;

    private boolean connected;
    private InputStream consoleInput;
    private OutputStream consoleOutput;

    private SessionUserInfo userInfo;

    private final String log = "SshConnection";

    public SshConnection(SessionUserInfo user)
    {
        console = console;
        JSch jsch = new JSch();
        session = null;
        channel = null;

        connected = false;

        consoleInput = null;
        consoleOutput = null;

        userInfo = null;
        try
        {
            session = jsch.getSession(user.getUser(), user.getHost(), 22);
        }
        catch(Exception e)
        {
            Log.d(log, "Exception caught while creating jsch session" + e.getMessage());
            session = null;
        }
        session.setUserInfo(user);
        userInfo = user;
    }

    public boolean isConnected()
    {
        return connected;
    }

    public void disableHostChecking()
    {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        Log.d(log, "Host checking disabled");
        session.setConfig(config);
    }


    public boolean connectAsShell()
    {
        boolean ret = false;
        try
        {
            if(session != null)
            {
                session.connect();
                connected = true;
                channel = session.openChannel("shell");
                consoleInput = channel.getInputStream();
                consoleOutput = channel.getOutputStream();
                setTermIn(consoleInput);
                setTermOut(consoleOutput);
                channel.connect();

                Log.d(log, "SSH Connected");
                ret = true;
            }
        }
        catch(Exception  e)
        {
            Log.d(log, "Exception caught while initiating SSH connection" + e.getMessage());
            ret = false;
            connected = false;
        }
        return ret;
    }

    //not supported anymore!
    public String executeCommand(String command)
    {
        String ret = null;
        if(!connected)
        {
            return ret;
        }
            try
            {
                //open channel ready to send input

                ((ChannelExec)channel).setCommand(command);


                StringBuilder stringBuilder = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(consoleInput));

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
                connected = false;
            }
            channel.disconnect();
            return ret;
    }

    //not tested, need to be worked on
    boolean sendFile(File[] files, SftpProgressMonitor monitor)
    {
        boolean ret = false;
        if(!connected)
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
        String decoded = "ERROR";
        try
        {
        decoded = new String(buffer, "UTF-8");
        }
        catch(Exception e)
        {
        }
        Log.d(log, "Processing Input " + decoded);
        super.processInput(buffer, offset, count);
    }

 }

