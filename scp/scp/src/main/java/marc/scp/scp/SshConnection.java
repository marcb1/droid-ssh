package marc.scp.scp;

/**
 * Created by Marc on 5/1/14.
 */
public class SshConnection
{
    //jsch session
    private Session session;

    public SshConnection(String username, String password, String hostname, int portNumber)
    {
        JSch jsch = new JSch();
        session = jsch.getSession(username, hostname, portNumber);
        session.setPassword(password);
    }

    public boolean connect()
    {
        //try to catch exceptions
        session.connect();
    }

    public String executeCommand(String command)
    {
        ChannelExec channelssh = (ChannelExec)session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand(command);
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

    public void disableHostChecking()
    {
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);
    }
 }

