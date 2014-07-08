package com.t3hh4xx0r.leash;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Listens for disconnection from home device.
 */
public class WearableEventService extends WearableListenerService {

    private static final String FIELD_ALARM_ON = "alarm_on";

    private AudioManager mAudioManager;
    private static int mOrigVolume;
    private int mMaxVolume;
    private Uri mAlarmSound;
    private MediaPlayer mMediaPlayer;
    static Vibrator vibe;

    @Override
    public void onCreate() {
        super.onCreate();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        mOrigVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
//        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
//        mAlarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        // Reset the alarm volume to the user's original setting.
//        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, mOrigVolume, 0);
//        mMediaPlayer.release();
        super.onDestroy();
    }

    private static final String TAG = "ExampleFindPhoneApp";

    private static final int FORGOT_PHONE_NOTIFICATION_ID = 1;

    @Override
    public void onPeerDisconnected(com.google.android.gms.wearable.Node peer) {
        // Create a "forgot phone" notification when phone connection is broken.
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enable_leash", true)) {

            Intent toggleAlarmOperation = new Intent(this, SendMessageService.class);
            toggleAlarmOperation.setAction(SendMessageService.ACTION_LOCAL_ALARM_OFF);

            PendingIntent toggleAlarmIntent = PendingIntent.getService(this, 0, toggleAlarmOperation,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Notification.Action alarmAction = new Notification.Action(R.drawable.ic_launcher, "Turn Off", toggleAlarmIntent);

            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.left_phone_title))
                    .setContentText(getString(R.string.left_phone_content))
                    .setVibrate(new long[]{0, 200})  // Vibrate for 200 milliseconds.
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLocalOnly(true)
                    .extend(new Notification.WearableExtender()
                            .addAction(alarmAction)
                            .setContentAction(1))
                            .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX);
            Notification card = notificationBuilder.build();
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                    .notify(FORGOT_PHONE_NOTIFICATION_ID, card);
            vibe.vibrate(new long[]{0, 500, 250}, 0);

        }
    }

    @Override
    public void onPeerConnected(com.google.android.gms.wearable.Node peer) {
        // Remove the "forgot phone" notification when connection is restored.
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .cancel(FORGOT_PHONE_NOTIFICATION_ID);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d("MESSAGE RECEIVED", messageEvent.getPath());
        if (messageEvent.getPath().equals("find_wear_on")) {
            vibe.vibrate(new long[]{0, 500, 250}, 0);
            WearNotificationManager.showWearNotification(this);
        } else if (messageEvent.getPath().equals("find_wear_off")) {
            vibe.cancel();
        } else if (messageEvent.getPath().equals("enable_leash")) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("enable_leash", true).commit();
        } else if (messageEvent.getPath().equals("disable_leash")) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("enable_leash", false).commit();
        }
//        if (messageEvent.getPath().equals("find_wear_on")) {
//            mOrigVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
//            mMediaPlayer.reset();
//            // Sound alarm at max volume.
//            mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, mMaxVolume, 0);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//            try {
//                mMediaPlayer.setDataSource(getApplicationContext(), mAlarmSound);
//                mMediaPlayer.prepare();
//            } catch (IOException e) {
//                Log.e(TAG, "Failed to prepare media player to play alarm.", e);
//            }
//            mMediaPlayer.start();
//
//        } else if (messageEvent.getPath().equals("find_wear_off")) {
//            // Reset the alarm volume to the user's original setting.
//            mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, mOrigVolume, 0);
//            if (mMediaPlayer.isPlaying()) {
//                mMediaPlayer.stop();
//            }
//        }
    }

}
