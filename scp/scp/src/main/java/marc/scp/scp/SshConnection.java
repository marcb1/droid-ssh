package marc.scp.scp;

import com.jcraft.jsch.*;
import java.io.ByteArrayOutputStream;

/**
 * Created by Marc on 5/1/14.
 */
public class SshConnection
{
    //jsch session
    private Session session;
    private Channel channel;


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

    public boolean connect()
    {
        boolean ret = true;
        try
        {
            session.connect(30000);
        }
        catch(Exception e)
        {
            System.out.println(e);
            ret = false;
        }
        return ret;
    }

    public boolean openChannel()
    {
        boolean ret = true;
        try
        {
            channel = session.openChannel("exec");
            channel.connect();
        }
        catch(Exception e)
        {
            System.out.println(e);
            ret = false;
        }
        return ret;
    }

    public String executeCommand(String command)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channel.setOutputStream(baos);

        // Execute command
        ((ChannelExec)channel).setCommand(command);
        return baos.toString();
    }

    public void disableHostChecking()
    {
       // Properties prop = new Properties();
       // prop.put("StrictHostKeyChecking", "no");
        //session.setConfig(prop);
    }
 }

