package com.t3hh4xx0r.leash;

import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;


public class LeashActivity extends Activity {

    private static final int FIND_PHONE_NOTIFICATION_ID = 2;
    private static Notification.Builder notification;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();

        setContentView(R.layout.activity_main);

        CardFragmentPagerAdapter adapter = new CardFragmentPagerAdapter(this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void findPhone() {
        Intent toggleAlarmOperation = new Intent(this, FindPhoneService.class);
        toggleAlarmOperation.setAction(FindPhoneService.ACTION_TOGGLE_ALARM);
        startService(toggleAlarmOperation);
        showNotification(false);
        updateNotification(this, getString(R.string.turn_alarm_off));
        finish();
    }

    public void openOnPhone() {
        new OpenOnPhoneTask(mGoogleApiClient, this).execute();
        finish();
    }

    public void showNotification(boolean close) {
        // Create a notification with an action to toggle an alarm on the phone.
        Intent toggleAlarmOperation = new Intent(this, FindPhoneService.class);
        toggleAlarmOperation.setAction(FindPhoneService.ACTION_TOGGLE_ALARM);
        PendingIntent toggleAlarmIntent = PendingIntent.getService(this, 0, toggleAlarmOperation,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Action alarmAction = new Action(R.drawable.alarm_action_icon, "", toggleAlarmIntent);
        // This intent turns off the alarm if the user dismisses the card from the wearable.
        Intent cancelAlarmOperation = new Intent(this, FindPhoneService.class);
        cancelAlarmOperation.setAction(FindPhoneService.ACTION_CANCEL_ALARM);
        PendingIntent cancelAlarmIntent = PendingIntent.getService(this, 0, cancelAlarmOperation,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // Use a spannable string for the notification title to resize it.
        SpannableString title = new SpannableString(getString(R.string.app_name));
        title.setSpan(new RelativeSizeSpan(0.85f), 0, title.length(), Spannable.SPAN_POINT_MARK);
        notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(getString(R.string.turn_alarm_on))
                .setSmallIcon(R.drawable.ic_launcher)
                .setVibrate(new long[] {0, 50})  // Vibrate to bring card to top of stream.
                .setDeleteIntent(cancelAlarmIntent)
                .extend(new Notification.WearableExtender()
                        .addAction(alarmAction)
                        .setContentAction(0)
                        .setHintHideIcon(true))
                .setLocalOnly(true)
                .setPriority(Notification.PRIORITY_MAX);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(FIND_PHONE_NOTIFICATION_ID, notification.build());
        if (close) {
            finish();
        }
    }

    /**
     * Updates the text on the wearable notification. This is used so the notification reflects the
     * current state of the alarm on the phone. For instance, if the alarm is turned on, the
     * notification text indicates that the user can tap it to turn it off, and vice-versa.
     *
     * @param context
     * @param notificationText The new text to display on the wearable notification.
     */
    public static void updateNotification(Context context, String notificationText) {
        if (notification != null) {
            notification.setContentText(notificationText);
            ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE))
                    .notify(FIND_PHONE_NOTIFICATION_ID, notification.build());
        }
    }

}