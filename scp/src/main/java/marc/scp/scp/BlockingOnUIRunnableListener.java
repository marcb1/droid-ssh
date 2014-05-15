package marc.scp.scp;

/**
 * Events for blocking runnable executing on UI thread
 *
 * @author
 *
 */
public interface BlockingOnUIRunnableListener
{
    /**
     * Code to execute on UI thread, runnable object must be synchronized on to notify waiting threads before this function returns
     * otherwise thread will wait indefinitely
     */
    public void onRunOnUIThread(Runnable runnable);
}