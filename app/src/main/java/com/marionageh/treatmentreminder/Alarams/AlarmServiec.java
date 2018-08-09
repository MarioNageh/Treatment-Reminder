package com.marionageh.treatmentreminder.Alarams;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.app.Notification;

import com.marionageh.treatmentreminder.MainScreen;
import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

public class AlarmServiec extends IntentService {
    private static final String TAG = AlarmServiec.class.getSimpleName();
    PendingIntent operation;
    private static final String Treatment_REMINDER_NOTIFICATION_CHANNEL_ID = "Notifaction_Reminder_Treatment";
    private static final int Treatment_REMINDER_NOTIFICATION_ID = 254;


    public AlarmServiec() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String id_treatment = intent.getAction();
        Log.e("MARIooOO", id_treatment);
        int id = Integer.valueOf(id_treatment);

        //Display a notification to view the task details
        Intent action = new Intent(this, MainScreen.class);
        operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        TreatmentDao treatmentDao = appDatabase.getTreatmentDao();
        Treatment treatment = treatmentDao.SelectTreatmentId(id);

        CreatNotication(treatment);
    }

    private void CreatNotication(Treatment treatment) {
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Treatment_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_notifitaction)
                .setLargeIcon(largeIcon(this))
                .setContentTitle(treatment.getName())
                .setContentText(treatment.getNote())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(treatment.getNote()
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