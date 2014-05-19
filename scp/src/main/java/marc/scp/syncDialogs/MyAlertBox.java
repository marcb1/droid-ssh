package marc.scp.syncDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import marc.scp.sshutils.SessionUserInfo;

/**
 * Created by Marc on 5/14/14.
 */
public class MyAlertBox  implements BlockingOnUIRunnableListener
{
    Activity activityParent;
    SessionUserInfo handler;
    String message;
    String title;

    public MyAlertBox(Activity parent, String boxTitle, String msg, SessionUserInfo handle)
    {
        message = msg;
        handler = handle;
        activityParent = parent;
        title = boxTitle;
    }

    public void onRunOnUIThread(final Runnable runnable)
    {
        //Unfortunately, this could throw an exception if this thread tries to create an alert while the activity is not running
        new AlertDialog.Builder(activityParent)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                dialog.dismiss();
                                activityParent.finish();
                            }
                        })
                .show();



        synchronized ( runnable )
        {
            runnable.notifyAll();
        }
    }
}