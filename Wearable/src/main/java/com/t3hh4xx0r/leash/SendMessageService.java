package com.t3hh4xx0r.leash;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

/**
 * Creates a sound on the paired phone to find it.
 */
public class SendMessageService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ExampleFindPhoneApp";

    public static final String ACTION_REMOTE_ALARM_ON = "action_remote_alarm_on";
    public static final String ACTION_REMOTE_ALARM_OFF = "action_remote_alarm_off";
    public static final String ACTION_LOCAL_ALARM_ON = "action_local_alarm_on";
    public static final String ACTION_LOCAL_ALARM_OFF = "action_local_alarm_off";
    public static final String ACTION_OPEN = "open";

    // Timeout for making a connection to GoogleApiClient (in milliseconds).
    private static final long CONNECTION_TIME_OUT_MS = 100;
    private GoogleApiClient mGoogleApiClient;

    public SendMessageService() {
        super(SendMessageService.class.getSimpleName());
    }

    Intent messageIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onHandleIntent(Intent messageIntent) {
        this.messageIntent = messageIntent;
        mGoogleApiClient.connect();
        Log.d("THE ACTION MAN", messageIntent.getAction().toString());
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mGoogleApiClient.isConnected()) {
            if (messageIntent.getAction().equals(ACTION_REMOTE_ALARM_ON)) {
                NotificationManager.showMainNotificationStop(this);
                new SendMessageTask(mGoogleApiClient, this, SendMessageTask.SendMessageType.FIND_ON).execute();
            } else if (messageIntent.getAction().equals(ACTION_REMOTE_ALARM_OFF)) {
                NotificationManager.dismiss(this, NotificationManager.PHONE_CONTROL_NOTIFICATION);
                new SendMessageTask(mGoogleApiClient, this, SendMessageTask.SendMessageType.FIND_OFF).execute();
            } else if (messageIntent.getAction().equals(ACTION_LOCAL_ALARM_OFF)) {
                if (WearableEventService.vibe != null) {
                    WearableEventService.vibe.cancel();
                }
                NotificationManager.dismiss(this, NotificationManager.WEAR_CONTROL_NOTIFICATION);

            } else if (messageIntent.getAction().equals(ACTION_LOCAL_ALARM_ON)) {
                NotificationManager.showWearNotification(this);
            } else if (messageIntent.getAction().equals(ACTION_OPEN)) {
                new SendMessageTask(mGoogleApiClient, this, SendMessageTask.SendMessageType.OPEN).execute();
            }
        } else {
            Log.e(TAG, "Failed to toggle alarm on phone - Client disconnected from Google Play "
                    + "Services");
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

}
