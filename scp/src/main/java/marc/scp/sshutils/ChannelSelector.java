package marc.scp.sshutils;

import com.jcraft.jsch.ChannelSftp;

import marc.scp.constants.Constants;

import java.io.File;


public class ChannelSelector implements ChannelSftp.LsEntrySelector
{
    public boolean      _result;
    private File        _file;

    private static final String LOG = Constants.LOG_PREFIX + "ChannelSelector";

    public ChannelSelector(File f)
    {
        _file = f;
        _result = false;
    }

    public int select(ChannelSftp.LsEntry entry)
    {
        // if names are equal and sizes are equal let's not upload the _file
        if((_file.length() == entry.getAttrs().getSize()) && (_file.getName().equals(entry.getFilename())) )
        {
            _result = true;
            return BREAK;
        }
        return CONTINUE;
    }
}
