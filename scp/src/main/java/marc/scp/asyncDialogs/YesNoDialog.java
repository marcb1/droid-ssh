package marc.scp.asyncDialogs;

import android.content.DialogInterface;

/**
 * Created by Marc on 5/16/14.
 */
public abstract class YesNoDialog
{
    public abstract void PositiveMethod(DialogInterface dialog, int id);

    public void NegativeMethod(DialogInterface dialog, int id)
    {
        dialog.dismiss();
    }
}
