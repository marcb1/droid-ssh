package marc.scp.syncDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class AlertBox implements BlockingOnUIRunnableListener
{
    private Activity            _activityParent;
    private String              _message;
    private String              _title;
    public DialogResponse       _userResponse;

    public AlertBox(Activity parent, String boxTitle, String msg)
    {
        _message = msg;
        _activityParent = parent;
        _title = boxTitle;
        _userResponse = new DialogResponse();
    }

    public DialogResponse getUserResponse()
    {
        return _userResponse;
    }

    public void onRunOnUIThread(final Runnable runnable)
    {
        //Unfortunately, this could throw an exception if this thread tries to create an alert while the activity is not running
        new AlertDialog.Builder(_activityParent)
                .setMessage(_message)
                .setTitle(_title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                dialog.dismiss();
                                _activityParent.finish();
                            }
                        })
                .show();

        synchronized ( runnable )
        {
            runnable.notifyAll();
        }
    }
}