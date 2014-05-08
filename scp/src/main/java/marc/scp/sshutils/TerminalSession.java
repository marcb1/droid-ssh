package marc.scp.sshutils;
import jackpal.androidterm.emulatorview.TermSession;
import java.io.*;

/**
 * Created by Marc on 5/8/14.
 */
public class TerminalSession extends TermSession
{
    public TerminalSession(InputStream termIn, OutputStream termOut) {
        setTermIn(termIn);
        setTermOut(termOut);
    }


}
