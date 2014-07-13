package marc.scp.fileutils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Marc on 6/15/2014.
 */
public class FileUtils
{
    //Copy fromFile -> toFile, overwrite toFile if it already exists
    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException
    {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try
        {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        }
        finally
        {
            try
            {
                if (fromChannel != null)
                {
                    fromChannel.close();
                }
            }
            finally
            {
                if (toChannel != null)
                {
                    toChannel.close();
                }
            }
        }
    }
}

