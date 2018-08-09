package com.marionageh.treatmentreminder.Alarams;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.customClasses.ModeRepetation;
import com.marionageh.treatmentreminder.models.Treatment;

import java.util.Calendar;
import java.util.Date;

import static com.marionageh.treatmentreminder.AddTreatment.milDay;
import static com.marionageh.treatmentreminder.AddTreatment.milHour;
import static com.marionageh.treatmentreminder.AddTreatment.milMinute;
import static com.marionageh.treatmentreminder.AddTreatment.milMonth;
import static com.marionageh.treatmentreminder.AddTreatment.milWeek;

public class AlarmAdapter {


    public void addAlarm(Context context, long alarmTime, int id) {
        AlarmManager manager = AlarmManagerMaker.getAlarmManager(context);

        PendingIntent operation = MakePendingIntent(context, id);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    public void RepeatAlarm(Context context, long alarmTime, int id, long RepeatTime) {
        AlarmManager manager = AlarmManagerMaker.getAlarmManager(context);
        PendingIntent operation = MakePendingIntent(context, id);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }

    public void deleteAlarm(Context context, int id) {
        AlarmManager manager = AlarmManagerMaker.getAlarmManager(context);
        PendingIntent operation = MakePendingIntent(context, id);
        manager.cancel(operation);

    }


    public static PendingIntent MakePendingIntent(Context context,int id) {
        Intent action = new Intent(context, AlarmServiec.class);
        action.setAction(""+id);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }





}
