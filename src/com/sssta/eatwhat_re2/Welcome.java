package com.sssta.eatwhat_re2;

import java.util.Timer;
import java.util.TimerTask;


import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class Welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(0x7f030000);
        final Intent intent2 = new Intent(Welcome.this,
				MainActivity.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(intent2);
				finish(); // Ö´ÐÐ
			}
		};
	timer.schedule(task, 1000 * 2);
    }    
}
