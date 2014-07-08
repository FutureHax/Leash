package com.t3hh4xx0r.leash;

import android.app.Activity;
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
public class WearNotificationManager {
    public static Notification.Builder notification;
    public static final int PHONE_CONTROL_NOTIFICATION = 2;

    public static final int WEAR_CONTROL_NOTIFICATION = 3;

    public static void showMainNotificationStop(Context ctx) {
        // Create a notification with an action to toggle an alarm on the phone.
        Intent toggleAlarmOperation = new Intent(ctx, SendMessageService.class);
            toggleAlarmOperation.setAction(SendMessageService.ACTION_REMOTE_ALARM_OFF);

        PendingIntent toggleAlarmIntent = PendingIntent.getService(ctx, 0, toggleAlarmOperation,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Action alarmAction = new Notification.Action(R.drawable.alarm_action_icon, "", toggleAlarmIntent);
        // Use a spannable string for the notification title to resize it.
        SpannableString title = new SpannableString(ctx.getString(R.string.app_name));
        title.setSpan(new RelativeSizeSpan(0.85f), 0, title.length(), Spannable.SPAN_POINT_MARK);
        notification = new Notification.Builder(ctx)
                .setContentTitle(title)
                .setContentText(ctx.getString(R.string.turn_alarm_off))
                .setSmallIcon(R.drawable.ic_launcher)
                .setVibrate(new long[] {0, 50})  // Vibrate to bring card to top of stream.
                .extend(new Notification.WearableExtender()
                        .addAction(alarmAction)
                        .setContentAction(0)
                        .setHintHideIcon(true))
                .setLocalOnly(true)
                .setPriority(Notification.PRIORITY_MAX);
        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE))
                .notify(PHONE_CONTROL_NOTIFICATION, notification.build());
    }


    public static void showMainNotificationStart(Context ctx) {
        // Create a notification with an action to toggle an alarm on the phone.
        Intent toggleAlarmOperation = new Intent(ctx, SendMessageService.class);

            toggleAlarmOperation.setAction(SendMessageService.ACTION_REMOTE_ALARM_ON);

        PendingIntent toggleAlarmIntent = PendingIntent.getService(ctx, 0, toggleAlarmOperation,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Action alarmAction = new Notification.Action(R.drawable.alarm_action_icon, "", toggleAlarmIntent);
        // Use a spannable string for the notification title to resize it.
        SpannableString title = new SpannableString(ctx.getString(R.string.app_name));
        title.setSpan(new RelativeSizeSpan(0.85f), 0, title.length(), Spannable.SPAN_POINT_MARK);
        notification = new Notification.Builder(ctx)
                .setContentTitle(title)
                .setContentText(ctx.getString(R.string.turn_alarm_on))
                .setSmallIcon(R.drawable.ic_launcher)
                .setVibrate(new long[] {0, 50})  // Vibrate to bring card to top of stream.
                .extend(new Notification.WearableExtender()
                        .addAction(alarmAction)
                        .setContentAction(0)
                        .setHintHideIcon(true))
                .setLocalOnly(true)
                .setPriority(Notification.PRIORITY_MAX);
        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE))
                .notify(PHONE_CONTROL_NOTIFICATION, notification.build());
    }

    public static void showWearNotification(Context ctx) {
        // Create a notification with an action to toggle an alarm on the phone.
        Intent toggleAlarmOperation = new Intent(ctx, SendMessageService.class);
            toggleAlarmOperation.setAction(SendMessageService.ACTION_LOCAL_ALARM_OFF);
        PendingIntent toggleAlarmIntent = PendingIntent.getService(ctx, 0, toggleAlarmOperation,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Action alarmAction = new Notification.Action(R.drawable.alarm_action_icon, "", toggleAlarmIntent);
        // Use a spannable string for the notification title to resize it.
        SpannableString title = new SpannableString(ctx.getString(R.string.app_name));
        title.setSpan(new RelativeSizeSpan(0.85f), 0, title.length(), Spannable.SPAN_POINT_MARK);
        notification = new Notification.Builder(ctx)
                .setContentTitle(title)
                .setContentText(ctx.getString(R.string.turn_alarm_off))
                .setSmallIcon(R.drawable.ic_launcher)
                .setVibrate(new long[] {0, 50})  // Vibrate to bring card to top of stream.
                .extend(new Notification.WearableExtender()
                        .addAction(alarmAction)
                        .setContentAction(0)
                        .setHintHideIcon(true))
                .setLocalOnly(true)
                .setPriority(Notification.PRIORITY_MAX);
        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE))
                .notify(WEAR_CONTROL_NOTIFICATION, notification.build());
    }

    public static void dismiss(Context ctx, int id) {
        ((android.app.NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE)).cancel(id);
    }
}
