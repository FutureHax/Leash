package com.t3hh4xx0r.leash;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Listens for disconnection from home device.
 */
public class DeviceEventService extends WearableListenerService {

    private static final String TAG = "Leash";

    Vibrator vibe;

    private static final int FORGOT_WEAR_NOTIFICATION_ID = 11;

    @Override
    public void onCreate() {
        super.onCreate();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onPeerDisconnected(com.google.android.gms.wearable.Node peer) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enable_leash_phone", true)) {
            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.left_wear_title))
                    .setContentText(getString(R.string.left_wear_content))
                    .setSmallIcon(R.drawable.ic_launcher)

//                        fix with api check first
//                .setLocalOnly(true)
                    .setPriority(Notification.PRIORITY_MAX);
            Notification card = notificationBuilder.build();
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                    .notify(FORGOT_WEAR_NOTIFICATION_ID, card);
            vibe.vibrate(new long[]{0, 500, 250}, 0);
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
            vibe.vibrate(new long[]{0, 500, 250}, 0);
        } else if (messageEvent.getPath().equals("find_phone_off")) {
            vibe.cancel();
        }
    }
}
