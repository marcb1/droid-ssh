package marc.scp.scp;

import com.jcraft.jsch.*;
import java.io.ByteArrayOutputStream;
import java.io.*;
import android.util.Log;

/**
 * Created by Marc on 5/1/14.
 */

public class SshConnection
{
    //jsch session
    private Session session;
    private Channel channel;

    //returned data
    ByteArrayOutputStream baos;
    private final String log = "SshConnection";


    public SshConnection(String username, String password, String hostname, int portNumber)
    {
        JSch jsch = new JSch();
        try
        {
            session = jsch.getSession(username, hostname, portNumber);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        session.setPassword(password);
    }

    public void disableHostChecking()
    {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    public boolean connect()
    {
        boolean ret = true;
        try
        {
          //  channel = session.openChannel("exec");
           // baos = new ByteArrayOutputStream();
          //  channel.setOutputStream(baos);
            session.connect(30000);
          //  channel.connect();
            Log.d(log, "SSH Connected");
        }
        catch(JSchException  e)
        {
            Log.d(log, e.getLocalizedMessage());
            ret = false;
        }
        return ret;
    }

    public String executeCommand(String command)
    {
        // Execute command
        ((ChannelExec)channel).setCommand(command);
        return baos.toString();
    }

    public void transferFile(FileInputStream file)
    {
        try
        {
            OutputStream out = channel.getOutputStream();
        }
        catch(Exception e)
        {
            System.out.println("B" + e);
        }
    }
 }

