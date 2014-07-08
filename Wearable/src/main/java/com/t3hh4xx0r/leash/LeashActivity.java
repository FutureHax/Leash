package com.t3hh4xx0r.leash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;


public class LeashActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        CardFragmentPagerAdapter adapter = new CardFragmentPagerAdapter(this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

    }

    public void findPhone() {
        Intent toggleAlarmOperation = new Intent(this, SendMessageService.class);
        toggleAlarmOperation.setAction(SendMessageService.ACTION_REMOTE_ALARM_ON);
        startService(toggleAlarmOperation);

        WearNotificationManager.showMainNotificationStop(this);
    }

    public void openOnPhone() {
        Intent openOperation = new Intent(this, SendMessageService.class);
        openOperation.setAction(SendMessageService.ACTION_OPEN);
        startService(openOperation);
        finish();
    }

}