package marc.scp.sshutils;

import android.util.Log;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import jackpal.androidterm.emulatorview.TermSession;

/**
 * Created by Marc on 5/18/2014.
 */
public class TerminalSession extends TermSession
{
    private ShellConnection conn;

    public TerminalSession(ShellConnection connection)
    {
        conn = connection;
        PipedInputStream i = null;
        PipedOutputStream ou = null;
        try
        {
            i = new PipedInputStream(connection.getOutputStream());
            ou = new PipedOutputStream(connection.getInputStream());
            //setTerminalStuff Here
            setTermIn(i);
            setTermOut(ou);
        }
        catch(Exception e)
        {
           // Log.d(log, "Exception caught while creating jsch session" + e.getMessage());
        }

    }

    @Override //called when data is processed from the input stream
    public void processInput(byte[] buffer, int offset, int count)
    {
        super.processInput(buffer, offset, count);
    }
}
