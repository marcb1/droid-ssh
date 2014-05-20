package marc.scp.scp;

import android.app.Fragment;
import android.os.Bundle;

import marc.scp.sshutils.TerminalSession;
import marc.scp.sshutils.TerminalView;

/**
 * Created by Marc on 5/19/2014.
 */
public class RetainedTerminal extends Fragment {

    // data object we want to retain
    private TerminalSession terminalSession;
    private TerminalView terminalview;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setTerminalSession(TerminalSession session)
    {
        terminalSession = session;
    }

    public void setTerminalView(TerminalView view)
    {
        terminalview = view;
    }

    public TerminalSession getTerminalSession() {
        return terminalSession;
    }


    public TerminalView getTerminalView() {
        return terminalview;
    }
}