package marc.scp.scp;

import android.app.Activity;

/**
 * Created by Marc on 5/14/14.
 */
public class BlockingOnUIRunnable
{
    private Activity activity;

    private BlockingOnUIRunnableListener listener;

    private Runnable uiRunnable;


    public BlockingOnUIRunnable( Activity activity, BlockingOnUIRunnableListener listener )
    {
        this.activity = activity;
        this.listener = listener;

        uiRunnable = new Runnable()
        {
            public void run()
            {
                // Execute custom code
                if ( BlockingOnUIRunnable.this.listener != null )
                {
                    BlockingOnUIRunnable.this.listener.onRunOnUIThread(this);
                }
            }
        };
    }


    public void startOnUiAndWait()
    {
        synchronized ( uiRunnable )
        {
            // Execute code on UI thread
            activity.runOnUiThread( uiRunnable );

            // Wait until runnable finished
            try
            {
                uiRunnable.wait();
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

}