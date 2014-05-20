package marc.scp.sshutils;

import com.jcraft.jsch.ChannelSftp;

import java.io.File;

/**
 * Created by Marc on 5/17/2014.
 */
public class ChannelSelector implements ChannelSftp.LsEntrySelector
{
    public boolean result;
    private File file;

    public ChannelSelector(File f)
    {
        file = f;
        result = false;
    }

    public int select(ChannelSftp.LsEntry entry)
    {
        //if names are equal and sizes are equal let's not upload the file
        if((file.length() == entry.getAttrs().getSize()) && (file.getName().equals(entry.getFilename())) )
        {
            result = true;
            return BREAK;
        }
        return CONTINUE;
    }
}
