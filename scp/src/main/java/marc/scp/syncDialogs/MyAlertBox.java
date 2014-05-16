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

    public MyAlertBox(Activity parent, String msg, SessionUserInfo handle)
    {
        message = msg;
        handler = handle;
        activityParent = parent;
    }

    public void onRunOnUIThread(final Runnable runnable)
    {
        new AlertDialog.Builder(activityParent)
                .setMessage(message)
                .setTitle("Error")
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