package ru.d2t.android.dota2timers;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    static final int roshanT = 11;
    static final int glyphT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /*int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }*/
        setContentView(R.layout.activity_main);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        HashMap<Integer,String> roshanNotifications = new HashMap<Integer,String>();
        roshanNotifications.put(6,"Рошан скоро появится!");
        roshanNotifications.put(0,"Pошан появился!");
        HashMap<Integer,String> glyphNotifications = new HashMap<Integer,String>();
        glyphNotifications.put(0,"У врага появился Глиф!");
        int roshResID = getResources().getIdentifier("roshan" , "drawable", getPackageName());
        int glyphResID = getResources().getIdentifier("glyph" , "drawable", getPackageName());
        Timer roshanTimer = new Timer(this, roshResID, roshanT, roshanNotifications);
        Timer glyphTimer = new Timer(this, glyphResID, glyphT, glyphNotifications);
        roshanTimer.Display();
        glyphTimer.Display();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
