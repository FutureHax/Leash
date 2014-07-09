package com.t3hh4xx0r.leash;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Listens for disconnection from home device.
 */
public class PhoneEventService extends WearableListenerService {


    private static final String TAG = "Leash";


    Uri toneUri;

    @Override
    public void onCreate() {
        super.onCreate();
        String uriString = PreferenceManager.getDefaultSharedPreferences(this).getString("notification_tone", null);
        if (uriString != null) {
            toneUri = Uri.parse(uriString);
        }
    }

    @Override
    public void onPeerDisconnected(com.google.android.gms.wearable.Node peer) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enable_leash_phone", true)) {
            Intent toggleAlarmOperation = new Intent(this, PhoneSendMessageService.class);
            toggleAlarmOperation.setAction(PhoneSendMessageService.ACTION_LOCAL_ALARM_OFF);

            PendingIntent toggleAlarmIntent = PendingIntent.getService(this, 0, toggleAlarmOperation,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.left_wear_title))
                    .setContentText(getString(R.string.left_wear_content))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .addAction(R.drawable.ic_launcher, "Turn off alarm", toggleAlarmIntent)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX);
            Notification card = notificationBuilder.build();
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                    .notify(PhoneNotificationManager.FORGOT_WEAR_NOTIFICATION_ID, card);
            Intent rIntent = new Intent(this, SoundAndVibrateAlertService.class);
            startService(rIntent);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d("MESSAGE RECEIVED", messageEvent.getPath());
        if (messageEvent.getPath().equals("open")) {
            Intent i = new Intent(this, SettingsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (messageEvent.getPath().equals("find_phone_on")) {
            Intent rIntent = new Intent(this, SoundAndVibrateAlertService.class);
            startService(rIntent);
            PhoneNotificationManager.showPhoneNotificationStop(this);
        } else if (messageEvent.getPath().equals("find_phone_off")) {
            Intent rIntent = new Intent(this, SoundAndVibrateAlertService.class);
            stopService(rIntent);
        }
    }
}
