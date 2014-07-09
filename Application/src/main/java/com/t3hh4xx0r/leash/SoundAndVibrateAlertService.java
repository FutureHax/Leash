package com.t3hh4xx0r.leash;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

public class SoundAndVibrateAlertService extends Service {
    private Ringtone ringtone;
    private Vibrator vibe;
    private Uri toneUri;
    PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "MyWakelockTag");
        wakeLock.acquire();


        String uriString = PreferenceManager.getDefaultSharedPreferences(this).getString("notification_tone", "null");
        if (!uriString.equals("null")) {
            toneUri = Uri.parse(uriString);
            ringtone = RingtoneManager.getRingtone(this, toneUri);
            ringtone.play();
        }
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(new long[]{0, 500, 250}, 0);



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (ringtone != null) {
            ringtone.stop();
        }
        vibe.cancel();
        wakeLock.release();
    }
}