package marc.scp.sshutils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;

/**
 * Created by Marc on 5/17/2014.
 */
public class TerminalView extends EmulatorView
{
    SshConnection conn;
    public TerminalView(Context paramContext, TermSession paramTermSession, DisplayMetrics paramDisplayMetrics)
    {
        super(paramContext, paramTermSession, paramDisplayMetrics);
    }

    public TerminalView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void addConnection(SshConnection c)
    {
        w = getVisibleWidth();
        h = getVisibleHeight();
        conn = c;
    }

    int w;
    int h;
    public void refreshScreen()
    {
        if(conn.channelShell != null)
        {
            conn.channelShell.setPtySize(this.mVisibleColumns, this.mVisibleRows, w, h);
        }
    }

    @Override
    public void setTextSize(int paramInt)
    {
        super.setTextSize(paramInt);
        super.updateSize(true);
        refreshScreen();
    }

    @Override
    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
        refreshScreen();
        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    }

}
