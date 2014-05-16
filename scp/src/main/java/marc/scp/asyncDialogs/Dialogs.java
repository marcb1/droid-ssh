package marc.scp.asyncDialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by Marc on 5/16/14.
 */

public class Dialogs
{
    public static void getConfirmDialog(Context mContext, String msg, String positiveBtnCaption,
                                        String negativeBtnCaption, boolean isCancelable, final YesNoDialog target)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage(msg).setCancelable(false).setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                target.PositiveMethod(dialog, id);
            }
        }).setNegativeButton(negativeBtnCaption, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                target.NegativeMethod(dialog, id);
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
                public void onCancel(DialogInterface arg0)
                {
                    target.NegativeMethod(null, 0);
                }
            });
        }
    }

    public static boolean toastIfEmpty(String content, Context mContext, String msg)
    {
        boolean ret = false;
        if(content.matches(""))
        {
            ret = true;
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        }
        return ret;
    }

}
