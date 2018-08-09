package com.marionageh.treatmentreminder.Alarams;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.customClasses.CustomAsyanTask;
import com.marionageh.treatmentreminder.customClasses.ModeRepetation;
import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.marionageh.treatmentreminder.AddTreatment.milDay;
import static com.marionageh.treatmentreminder.AddTreatment.milHour;
import static com.marionageh.treatmentreminder.AddTreatment.milMinute;
import static com.marionageh.treatmentreminder.AddTreatment.milMonth;
import static com.marionageh.treatmentreminder.AddTreatment.milWeek;

public class AlarmWhenStartSecviec extends Service implements CustomAsyanTask.CustomAsyncListener {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Getting data from database
//        AppDatabase appDatabase = AppDatabase.getInstance(this);
//        TreatmentDao treatmentDao = appDatabase.getTreatmentDao();
//        List<Treatment> treatmentList = treatmentDao.getAll();

        new CustomAsyanTask(this,this).execute();
        Log.d("MarioooooBreadcaos","1");



    }

    private void calculateDates(Treatment treatment) {
//        mDate = mDay + "/" + mMonth + "/" + mYear;
//        mTime = mHour + ":" + mMinute;
        Log.d("MarioooooBreadcaos",treatment.getName());
        String date = treatment.getDate();
        if(date.equals(""))return;
        String[] Dates = date.split("/");
        int mMonth, mDay, mYear;
        mDay = Integer.valueOf(Dates[0]);
        mMonth = Integer.valueOf(Dates[1]);
        mYear = Integer.valueOf(Dates[2]);
        String time = treatment.getTime();
        if(time.equals(""))return;

        String[] times = time.split(":");
        int mHour, mMinute;
        mHour = Integer.valueOf(times[0]);
        mMinute = Integer.valueOf(times[1]);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);
        long selectedTimestamp = mCalendar.getTimeInMillis();
        int repetedMode = dedectTheType(getResources().getStringArray(R.array.Types_Repetaion), treatment.getRepeat_type());
        long mRepeatTime = 0;
        switch (repetedMode) {
            case ModeRepetation.MINUTE:
                mRepeatTime = repetedMode * milMinute;
                break;
            case ModeRepetation.Hour:
                mRepeatTime = repetedMode * milHour;

                break;
            case ModeRepetation.Day:
                mRepeatTime = repetedMode * milDay;

                break;
            case ModeRepetation.Week:
                mRepeatTime = repetedMode * milWeek;

                break;
            case ModeRepetation.Month:
                mRepeatTime = repetedMode * milMonth;
                break;
        }

        long millisss = new Date().getTime();

        if (treatment.isActive()) {
            if (treatment.isRepeat()) {
                new AlarmAdapter().RepeatAlarm(getApplicationContext(), selectedTimestamp, treatment.getId(), mRepeatTime);
            } else if (!treatment.isRepeat()) {
                if(millisss<selectedTimestamp)
                new AlarmAdapter().addAlarm(getApplicationContext(), selectedTimestamp, treatment.getId());
            }
        } else {
            new AlarmAdapter().deleteAlarm(getApplicationContext(), treatment.getId());
        }


    }

    public int dedectTheType(String[] types, String type) {
        int counter = 0;
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(type)) {
                counter = i;
                break;
            }
        }
        return counter;
    }

    @Override
    public void onListReceived(List<Treatment> treatmentList) {
        for (int i = 0; i < treatmentList.size(); i++) {
            calculateDates(treatmentList.get(i));
        }
    }
}