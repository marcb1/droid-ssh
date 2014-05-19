package marc.scp.asyncNetworkTasks;

/**
 * Created by Marc on 5/16/14.
 * interface that connectTasks (async network tasks) use to notify caller of result
 */


public interface IConnectionNotifier
{
   abstract public void connectionResult(boolean result);
}
