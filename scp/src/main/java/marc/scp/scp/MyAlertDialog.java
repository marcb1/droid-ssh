package marc.scp.scp;


import android.app.Activity;
import android.content.DialogInterface;
import marc.scp.sshutils.SessionUserInfo;

public class MyAlertDialog implements BlockingOnUIRunnableListener
{
    Activity activityParent;
    SessionUserInfo handler;
    String message;
    public MyAlertDialog instance;

    public MyAlertDialog(Activity parent, String msg, SessionUserInfo handle)
    {
        message = msg;
        handler = handle;
        activityParent = parent;
        this.instance = this;
    }

    public void onRunOnUIThread(final Runnable runnable)
    {
        new android.app.AlertDialog.Builder(activityParent).setMessage(message)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        handler.alertBooleanResult = false;
                        synchronized ( runnable )
                        {
                            runnable.notifyAll();
                        }
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        handler.alertBooleanResult = true;
                        synchronized ( runnable )
                        {
                            runnable.notifyAll();
                        }
                    }
                })
                .create()
                .show();
    }
}