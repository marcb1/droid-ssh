package marc.scp.sshutils;
import marc.scp.scp.*;


import android.os.AsyncTask;

/**
 * Created by Marc on 5/1/14.
 */

import android.support.v7.app.ActionBarActivity;

public class SshConnectTask extends AsyncTask<SshConnection, Integer, String>
{
    DisplayMessageActivity handler;
    SshConnection conn;
    String message;

    public SshConnectTask(DisplayMessageActivity caller, String m)
    {
        handler = caller;
        conn = null;
        message = m;
    }

    protected String doInBackground(SshConnection... connection)
    {
        String ret = "";
        try
        {
            conn = connection[0];
            if(!conn.isConnected())
            {
                conn.connect();
            }
            System.out.println(message  + 1);
            ret = conn.executeCommand(message);
            System.out.println(message);
        }
        catch (Exception e)
        {
            ret = "error";
            System.out.println(e.getMessage());
           /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("User friendly text explaining what went wrong.");
            AlertDialog alert = builder.create();
            alert.show();*/
        }
        return ret;
    }


    protected void onProgressUpdate(String... progress)
    {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(String result)
    {
        //showDialog("Downloaded " + result + " bytes");
        System.out.println(result);
        handler.result(result);
    }

}