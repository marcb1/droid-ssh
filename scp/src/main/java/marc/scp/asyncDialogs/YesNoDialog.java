package marc.scp.asyncDialogs;

import android.content.DialogInterface;

/**
 * Created by Marc on 5/16/14.
 */

//Call back class for yes/no dialog
public abstract class YesNoDialog
{
    public abstract void PositiveMethod(DialogInterface dialog, int id);

    public void NegativeMethod(DialogInterface dialog, int id)
    {
        if(dialog != null)
        {
            dialog.dismiss();
        }
    }
}
