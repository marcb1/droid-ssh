package marc.scp.sshutils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;

/**
 * Created by Marc on 5/17/2014.
 * This is the terminal view
 */
public class TerminalView extends EmulatorView
{
    private ShellConnection conn;

    public TerminalView(Context paramContext, TermSession paramTermSession, DisplayMetrics paramDisplayMetrics)
    {
        super(paramContext, paramTermSession, paramDisplayMetrics);
    }

    public TerminalView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void addConnection(ShellConnection c)
    {
        conn = c;
    }

    public void refreshScreen()
    {
        super.updateSize(true);
        updatePTY();
    }

    @Override
    public void setTextSize(int paramInt)
    {
        //setTextSize calls updateSize(true) for us, no need to call again, just setPtySize
        super.setTextSize(paramInt);
        updatePTY();
    }

    @Override
    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        refreshScreen();
        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    @Override
    public void setTermType(String type)
    {
        super.setTermType(type);
    }

    private void updatePTY()
    {
        if(conn != null)
        {
            conn.setPtySize(getVisibleColumns(), getVisibleRows(), getWidth(), getHeight());
        }
    }

}
