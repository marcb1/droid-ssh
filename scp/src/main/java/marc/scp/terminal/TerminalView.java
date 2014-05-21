package marc.scp.terminal;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;
import marc.scp.sshutils.ShellConnection;

/**
 * Created by Marc on 5/17/2014.
 * This is the terminal view
 */
public class TerminalView extends EmulatorView
{
    private ShellConnection conn;
    private int textSize;

    public TerminalView(Context paramContext, TermSession paramTermSession, DisplayMetrics paramDisplayMetrics)
    {
        super(paramContext, paramTermSession, paramDisplayMetrics);
    }

    public TerminalView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    //called when orientation of screen changes
    public void copyOld(TerminalView old)
    {
        conn = old.getConnection();
        textSize = old.getTextSize();
    }

    public void addConnection(ShellConnection c)
    {
        conn = c;
    }

    public ShellConnection getConnection()
    {
        return conn;
    }

    public int getTextSize()
    {
        return textSize;
    }

    public void refreshScreen()
    {
        super.updateSize(true);
        updatePTY();
    }

    public void reduceSize()
    {
       textSize--;
        this.setTextSize(textSize);
    }

    public void increaseSize()
    {
        textSize++;
        this.setTextSize(textSize);
    }

    @Override
    public void setTextSize(int size)
    {
        textSize = size;
        //setTextSize calls updateSize(true) for us, no need to call again, just setPtySize
        super.setTextSize(size);
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
        conn.setPty(true);
    }

    private void updatePTY()
    {
        if(conn != null)
        {
            conn.setPtySize(getVisibleColumns(), getVisibleRows(), getWidth(), getHeight());
        }
    }

}
