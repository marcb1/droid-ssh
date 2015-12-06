package marc.scp.syncDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created by Marc on 5/21/2014.
 */
public class AlertInput implements BlockingOnUIRunnableListener
{
    private Activity        _activityParent;
    private String          _message;
    private String          _title;
    public DialogResponse   _userResponse;

    public AlertInput(Activity parent, String boxTitle, String msg)
    {
        _message = msg;
        _activityParent = parent;
        _title = boxTitle;
        _userResponse = new DialogResponse();
    }

    public void onRunOnUIThread(final Runnable runnable)
    {
        //Unfortunately, this could throw an exception if this thread tries to create an alert while the activity is not running
        AlertDialog.Builder alert = new AlertDialog.Builder(_activityParent);
        alert.setMessage(_message);
        alert.setTitle(_title);
        alert.setCancelable(false);
        final EditText input = new EditText(_activityParent);
        alert.setView(input);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String data = input.getText().toString();
                _userResponse.responseBoolean = true;
                _userResponse.responseString = data;
                synchronized ( runnable )
                {
                    runnable.notifyAll();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                _userResponse.responseBoolean = false;
                synchronized ( runnable )
                {
                    runnable.notifyAll();
                }
            }
        });
        alert.show();
    }
}