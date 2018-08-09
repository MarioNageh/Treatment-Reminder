package com.marionageh.treatmentreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    private static final String Treatment_REMINDER_NOTIFICATION_CHANNEL_ID = "Notifaction_Reminder_Treatment_enterOut";
    private static final int Treatment_REMINDER_NOTIFICATION_ID = 255;

    @Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, String.format("Error code : %d", geofencingEvent.getErrorCode()));
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        // Send the notification
        // if he leave the place we will send notfy to tell him to take medicine
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            CreatNotication(context);

        }
    }

    private void CreatNotication(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Treatment_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_notifitaction)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getResources().getString(R.string.donot_forget))
                .setContentText(context.getResources().getString(R.string.donot_forget_take))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(R.string.donot_forget_take)
                ))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(Treatment_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.treat);
        return largeIcon;
    }


}
