package com.t3hh4xx0r.leash;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

/**
 * Created by r2doesinc on 7/7/14.
 */
public class PhoneNotificationManager {
    public static Notification.Builder notification;
    public static final int PHONE_CONTROL_NOTIFICATION = 2;

    public static final int WEAR_CONTROL_NOTIFICATION = 3;
    public static final int FORGOT_WEAR_NOTIFICATION_ID = 11;

    public static void showPhoneNotificationStop(Context ctx) {
        // Create a notification with an action to toggle an alarm on the phone.
        Intent toggleAlarmOperation = new Intent(ctx, PhoneSendMessageService.class);
        toggleAlarmOperation.setAction(PhoneSendMessageService.ACTION_LOCAL_ALARM_OFF);

        PendingIntent toggleAlarmIntent = PendingIntent.getService(ctx, 0, toggleAlarmOperation,
                PendingIntent.FLAG_CANCEL_CURRENT);

//        Notification.Action alarmAction = new Notification.Action(R.drawable.ic_launcher, "TURN OFF", toggleAlarmIntent);
        // Use a spannable string for the notification title to resize it.
        SpannableString title = new SpannableString(ctx.getString(R.string.app_name));
        title.setSpan(new RelativeSizeSpan(0.85f), 0, title.length(), Spannable.SPAN_POINT_MARK);
        notification = new Notification.Builder(ctx)
                .setContentTitle(title)
                .setContentText("Finding your phone.")
                .addAction(R.drawable.ic_launcher, "Found my phone", toggleAlarmIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 50})  // Vibrate to bring card to top of stream.
                .setPriority(Notification.PRIORITY_MAX);

        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE))
                .notify(PHONE_CONTROL_NOTIFICATION, notification.build());
    }

    public static void showWearNotificationStop(Context ctx) {
        // Create a notification with an action to toggle an alarm on the phone.
        Intent toggleAlarmOperation = new Intent(ctx, PhoneSendMessageService.class);
        toggleAlarmOperation.setAction(PhoneSendMessageService.ACTION_REMOTE_ALARM_OFF);

        PendingIntent toggleAlarmIntent = PendingIntent.getService(ctx, 0, toggleAlarmOperation,
                PendingIntent.FLAG_CANCEL_CURRENT);

//        Notification.Action alarmAction = new Notification.Action(R.drawable.ic_launcher, "TURN OFF", toggleAlarmIntent);
        // Use a spannable string for the notification title to resize it.
        SpannableString title = new SpannableString(ctx.getString(R.string.app_name));
        title.setSpan(new RelativeSizeSpan(0.85f), 0, title.length(), Spannable.SPAN_POINT_MARK);
        notification = new Notification.Builder(ctx)
                .setContentTitle(title)
                .setContentText("Finding your Wear.")
                .addAction(R.drawable.ic_launcher, "Found my Wear", toggleAlarmIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 50})  // Vibrate to bring card to top of stream.
                .setPriority(Notification.PRIORITY_MAX);

        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE))
                .notify(WEAR_CONTROL_NOTIFICATION, notification.build());
    }

    public static void dismissAll(Context ctx) {
        Intent rIntent = new Intent(ctx, SoundAndVibrateAlertService.class);
        ctx.stopService(rIntent);
        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE)).cancel(PHONE_CONTROL_NOTIFICATION);
        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE)).cancel(WEAR_CONTROL_NOTIFICATION);
        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE)).cancel(FORGOT_WEAR_NOTIFICATION_ID);
    }
}
