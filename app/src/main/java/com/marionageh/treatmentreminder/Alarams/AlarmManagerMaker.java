package com.marionageh.treatmentreminder.Alarams;

import android.app.AlarmManager;
import android.content.Context;

public class AlarmManagerMaker {
    private static final String TAG = AlarmManagerMaker.class.getSimpleName();
    private static AlarmManager sAlarmManager;

    static synchronized AlarmManager getAlarmManager(Context context) {
        if (sAlarmManager == null) {
            sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return sAlarmManager;
    }
}
