package marc.scp.asyncNetworkTasks;

/**
 * Created by Marc on 5/16/14.
 */

//interface that connectTasks uses to notify of result
public interface IConnectionNotifier
{
   abstract public void connectionResult(boolean result);
}
