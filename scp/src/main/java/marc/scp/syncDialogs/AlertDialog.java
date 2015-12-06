package marc.scp.syncDialogs;


import android.app.Activity;
import android.content.DialogInterface;
import marc.scp.sshutils.SessionUserInfo;

public class AlertDialog implements BlockingOnUIRunnableListener
{
    Activity            _activityParent;
    String              _message;
    DialogResponse      _userResponse;

    public AlertDialog(Activity parent, String msg, SessionUserInfo handle)
    {
        _message = msg;
        _activityParent = parent;
        _userResponse = new DialogResponse();
    }

    public DialogResponse getDialogResponse()
    {
        return _userResponse;
    }

    public void onRunOnUIThread(final Runnable runnable)
    {
        android.app.AlertDialog alert =  new android.app.AlertDialog.Builder(_activityParent).setMessage(_message)
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        _userResponse.responseBoolean = false;
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
                        _userResponse.responseBoolean = true;
                        synchronized ( runnable )
                        {
                            runnable.notifyAll();
                        }
                    }
                })
                .create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}