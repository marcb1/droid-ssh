package marc.scp.sshutils;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.util.List;

/**
 * Created by Marc on 5/18/2014.
 */
public class SftpConnection extends SshConnection
{

    private ChannelSftp sftp;
    private String remotePath;

    private final String log = "SftpConnection";

    public SftpConnection(SessionUserInfo user)
    {
        super(user);

        sftp = null;
        remotePath = null;

        getUserInfo().setConnectionHandler(this);
    }

    @Override
    public boolean connect()
    {
        boolean ret = false;
        try
        {
            Session session = super.getSession();
            if((session != null) && (state == CONNECTION_STATE.DISCONNECTED))
            {
                Channel channel = super.getChannel();
                Log.d(log, "SFTP Connecting...");
                state = CONNECTION_STATE.CONNECTING;
                session.connect(5000);

                channel = session.openChannel("sftp");
                state = CONNECTION_STATE.CONNECTED;

                channel.connect(5000);
                sftp = (ChannelSftp)channel;

                Log.d(log, "SFTP Connected");
                ret = true;
            }
        }
        catch(JSchException e)
        {
            Log.d(log, "Exception caught while initiating SFTP connection: " + e.getMessage(), e);
            getUserInfo().handleException(e);
            ret = false;
            state = CONNECTION_STATE.DISCONNECTED;
            sftp = null;
        }
        return ret;
    }

    public boolean sendFiles(List<File> files, SftpProgressMonitor monitor)
    {
        boolean ret = false;
        try
        {
            sftp.setInputStream(null);
            ret = true;
            for (final File file : files)
            {
                try
                {
                    if(remotePath != null)
                    {
                        ChannelSelector select = new ChannelSelector(file);

                        sftp.ls(remotePath, select);
                        //if !result, a file with the same name and size is not in the remote directory so overriwte
                        if(!select.result)
                        {
                            sftp.put(file.getPath(), file.getName(), monitor, ChannelSftp.OVERWRITE);
                        }
                    }
                }
                catch (SftpException e)
                {
                    Log.e(log, "sendFiles", e);
                    ret = false;
                    break;
                }
            }
        }
        catch(Exception e)
        {
            ret = false;
        }
        return ret;
    }

    public boolean changeRemoteDirectory(String path)
    {
        boolean ret = false;
        if((state == state.DISCONNECTED) || (sftp == null))
        {
            return ret;
        }
        try
        {
            sftp.cd(path);
            remotePath = path;
            ret = true;
        }
        catch (SftpException e)
        {
            Log.e(log, "changeRemoteDirectory", e);
            ret = false;
        }
        return ret;
    }

    public boolean sendFile(File file)
    {
        boolean ret = false;
        if((state == state.DISCONNECTED) || (sftp == null))
        {
            return ret;
        }
        try
        {
            sftp.setInputStream(null);
            sftp.put(file.getPath(), file.getName(), ChannelSftp.APPEND);
            ret = true;
        }
        catch(Exception e)
        {
            ret = false;
        }
        return ret;
    }
}
