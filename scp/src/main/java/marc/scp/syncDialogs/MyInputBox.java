package marc.scp.syncDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import marc.scp.sshutils.SessionUserInfo;

/**
 * Created by Marc on 5/21/2014.
 */
public class MyInputBox implements BlockingOnUIRunnableListener
    {
        Activity activityParent;
        SessionUserInfo handler;
        String message;
        String title;

        public MyInputBox(Activity parent, String boxTitle, String msg, SessionUserInfo handle)
        {
            message = msg;
            handler = handle;
            activityParent = parent;
            title = boxTitle;
        }

        public void onRunOnUIThread(final Runnable runnable)
        {
            //Unfortunately, this could throw an exception if this thread tries to create an alert while the activity is not running
            AlertDialog.Builder alert = new AlertDialog.Builder(activityParent);
            alert.setMessage(message);
            alert.setTitle(title);
            alert.setCancelable(false);
            final EditText input = new EditText(activityParent);
            alert.setView(input);
            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    String password = input.getText().toString();
                    handler.setPassword(password);
                    handler.alertBooleanResult = true;
                    synchronized ( runnable )
                    {
                        runnable.notifyAll();
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    handler.alertBooleanResult = false;
                    synchronized ( runnable )
                    {
                        runnable.notifyAll();
                    }
                }
            });
            alert.show();
        }
    }

