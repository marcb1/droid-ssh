package marc.scp.asyncNetworkTasks;

/**
 * Created by Marc on 5/17/2014.
 * interface that UploadTask that uses to notify of upload _result
 */

public interface IUploadNotifier
{
    abstract public void transferResult(boolean result);
}
