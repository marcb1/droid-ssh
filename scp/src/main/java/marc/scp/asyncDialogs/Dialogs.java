package marc.scp.asyncDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;
import marc.scp.scp.R;


/**
 * Created by Marc on 5/16/14.
 * Class for re-usable dialogs, caller needs to implement YesNoDialog as callback
 */

public class Dialogs
{
    private String AlertDialogYes;
    private String AlertDialogNo;

    private static Dialogs instance;

    private Dialogs(Activity activity)
    {
        AlertDialogYes = activity.getString(R.string.yes);
        AlertDialogNo = activity.getString(R.string.no);
    }

    public static void init(Activity activity)
    {
        if(instance == null)
        {
            instance =  new Dialogs(activity);
        }
    }

    public static Dialogs getInstance()
    {
        return instance;
    }

    public AlertDialog getConfirmDialog(Context mContext, String msg, boolean isCancelable, final YesNoDialog target)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage(msg).setCancelable(false).setNegativeButton(AlertDialogNo, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                target.NegativeMethod(dialog, id);
            }
        }).setPositiveButton(AlertDialogYes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                target.PositiveMethod(dialog, id);
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();
        if (isCancelable)
        {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    target.NegativeMethod(dialog, 0);
                }
            });
        }
        return alert;
    }

    public boolean toastIfEmpty(String content, Context mContext, String msg)
    {
        boolean ret = false;
        if(content.matches(""))
        {
            ret = true;
            makeToast(mContext, msg);
        }
        return ret;
    }

    public void makeToast(Context mContext, String msg)
    {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void getAlertDialog(final Activity activity, String title, String msg, final boolean finishonOk)
    {
        new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                if (finishonOk) {
                                    activity.finish();
                                }
                            }
                        }
                )
                .show();
    }
}
