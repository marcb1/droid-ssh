package marc.scp.scp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.TextView;

public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.USERNAME);
        String password = intent.getStringExtra(MainActivity.PASSWORD);

        //construct ssh object and try to connect
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(username);

        // Set the text view as the activity layout
        setContentView(textView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //when user clicks on action bar item
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            //openSaved();
            return true;
        }
        else if (id == R.id.action_saved)
        {
            //openSettings();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

}
