package com.marionageh.treatmentreminder;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.marionageh.treatmentreminder.Alarams.AlarmAdapter;
import com.marionageh.treatmentreminder.customClasses.DeleteAsyanTask;
import com.marionageh.treatmentreminder.customClasses.ModeRepetation;
import com.marionageh.treatmentreminder.customClasses.UpdateAsyanTask;
import com.marionageh.treatmentreminder.models.Treatment;
import com.marionageh.treatmentreminder.ui.ReminderFragment;
import com.marionageh.treatmentreminder.widget.TreatmentWidgetService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.marionageh.treatmentreminder.MainScreen.REMINDER_FRAGMENT_TAG;
import static com.marionageh.treatmentreminder.MainScreen.fa;

public class AddTreatment extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, UpdateAsyanTask.CustomReminder, DeleteAsyanTask.CustomReminder {

    //For Comming data via bundel
    public static final String TREAMENT_SENDER = "TREATMENT";
    //Treatment
    Treatment treatment;

    ////////////////Views
    Toast msToast;
    @BindView(R.id.fab_turn_no_off)
    FloatingActionButton fab_turn_no_off;
    @BindView(R.id.toolbar_add_reminder)
    Toolbar toolbar_add_reminder;
    @BindView(R.id.reminder_title_add_reminder)
    EditText edit_title;
    @BindView(R.id.set_date)
    TextView set_date;
    @BindView(R.id.set_time)
    TextView set_time;
    @BindView(R.id.repeat_switch)
    Switch aSwitch;
    @BindView(R.id.set_repeat)
    TextView set_repeat;
    @BindView(R.id.set_repeat_no)
    TextView set_repeat_no;
    @BindView(R.id.set_repeat_type)
    TextView set_repeat_type;
    @BindView(R.id.set_Note)
    TextView set_Note;
///////////

    //For Geeting Current Date
    private Calendar mCalendar;
    //For Iamge
    private boolean mWorking;
    //Text from note and title
    private String mTitle;
    private String mNote;
    //For Switch Button
    private boolean mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private int repetedMode;
    //Date
    private int mYear, mMonth, mHour, mMinute, mDay;
    //String of Views
    private String mTime;
    private String mDate;


    // Saveing instats State
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_Note = "Note_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_Working = "Woring_key";
    private static final String KEY_Mode_Repatation = "reperation_key";


    // Constant values in milliseconds
    public static final long milMinute = 60000L;
    public static final long milHour = 3600000L;
    public static final long milDay = 86400000L;
    public static final long milWeek = 604800000L;
    public static final long milMonth = 2592000000L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            treatment = extras.getParcelable(TREAMENT_SENDER);
        } else {
            return;
        }

        PrepareActionBar();
        InitValues();
        OnRestoreState(savedInstanceState);
    }

    private void OnRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            mTitle = savedInstanceState.getString(KEY_TITLE);
            edit_title.setText(mTitle);
            mNote = savedInstanceState.getString(KEY_Note);
            set_Note.setText(mNote);
            mDate = savedInstanceState.getString(KEY_DATE);
            set_date.setText(mDate);
            mTime = savedInstanceState.getString(KEY_TIME);
            set_time.setText(mTime);
            mRepeat = savedInstanceState.getBoolean(KEY_REPEAT);
            mRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            set_repeat_no.setText(mRepeatNo);
            mRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            repetedMode = savedInstanceState.getInt(KEY_Mode_Repatation);
            set_repeat_type.setText(mRepeatType);
            mWorking = savedInstanceState.getBoolean(KEY_Working);


            if (mRepeat) {
                aSwitch.setChecked(true);
                set_repeat.setText(getResources().getString(R.string.Repetaion) + " " + mRepeatNo + " " + mRepeatType);

            } else {
                aSwitch.setChecked(false);
                set_repeat.setText(getResources().getString(R.string.repeat_off));

            }
            int off = R.drawable.ic_notifications_off_grey600_24dp;
            int on = R.drawable.ic_notifications_on_white_24dp;
            if (mWorking) {
                fab_turn_no_off.setIcon(on);


            } else {
                fab_turn_no_off.setIcon(off);
            }

        }
    }


    private void InitValues() {
        //For First Values
        // it think the treatment will take daily so repeted type  days and repete number for once

        if (treatment.getNote().equals(getResources().getString(R.string.Default_note)))
            mNote = getResources().getString(R.string.Default_note);
        else
            mNote = treatment.getNote();


        mWorking = treatment.isActive();

        int off = R.drawable.ic_notifications_off_grey600_24dp;
        int on = R.drawable.ic_notifications_on_white_24dp;
        if (mWorking) {
            fab_turn_no_off.setIcon(on);


        } else {
            fab_turn_no_off.setIcon(off);
        }


        mRepeat = treatment.isRepeat();

        if (treatment.getRepeat_no().equals("")) mRepeatNo = Integer.toString(1);
        else
            mRepeatNo = treatment.getRepeat_no();

        if (treatment.getRepeat_type().equals("")) mRepeatType = getResources().getString(R.string.Repeat_Type);
        else
            mRepeatType = treatment.getRepeat_type();
        repetedMode = ModeRepetation.Day;
        // we need to set the date now to views so that we will pick it

        if (treatment.getDate() != null) {
            mCalendar = Calendar.getInstance();
            mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            mMinute = mCalendar.get(Calendar.MINUTE);
            mYear = mCalendar.get(Calendar.YEAR);
            mMonth = mCalendar.get(Calendar.MONTH) + 1;
            mDay = mCalendar.get(Calendar.DATE);
            //
            mDate = mDay + "/" + mMonth + "/" + mYear;
            mTime = mHour + ":" + mMinute;

        } else {
            mDate = treatment.getDate();
            mTime = treatment.getTime();
        }
        edit_title.setText(treatment.getName());
        edit_title.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                edit_title.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //values will need it in database
        /*
        mTitle
        mNote
        mDate
        mTime
        mRepeat
        mRepeatNo
        mRepeatType
        mWorking
         */

        //Fill TextViews With default values or cooming from database
        if (treatment.getDate().equals("")) {
            //Will Set cuuren data
            set_date.setText(mDate);
        } else {
            // Will set comming date from database
            set_date.setText(treatment.getDate());

        }

        if (treatment.getNote().equals(getResources().getString(R.string.Default_note))) {
            //Will Set cuuren data
            set_Note.setText(getResources().getString(R.string.Default_note));
        } else {
            // Will set comming date from database
            set_Note.setText(treatment.getNote());

        }

        if (treatment.getTime().equals("")) {
            //Will Set cuuren data
            set_time.setText(mTime);
        } else {
            // Will set comming date from database
            set_time.setText(treatment.getTime());

        }
        if (treatment.isRepeat()) {
            aSwitch.setChecked(true);

            mRepeatNo = treatment.getRepeat_no();
            mRepeatType = treatment.getRepeat_type();
            repetedMode = dedectTheType(getResources().getStringArray(R.array.Types_Repetaion), mRepeatType);
            set_repeat.setText(getResources().getString(R.string.Repetaion) + " " + mRepeatNo + " " + mRepeatType);

        } else {
            mRepeat = false;
            aSwitch.setChecked(false);
            set_repeat_type.setText(mRepeatType);
            set_repeat_no.setText(mRepeatNo);
            set_repeat.setText(getResources().getString(R.string.repeat_off));

        }


    }

    private void PrepareActionBar() {
        setSupportActionBar(toolbar_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    //////////////////////For Menues //////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("Marioo", "menu");
        getMenuInflater().inflate(R.menu.menu_add_treatmentreminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //For Save Buuton
            case R.id.save_reminder_menu:
                savebutton();
                return true;
            // For Delelte Button
            case R.id.delete_reminder_menu:
                deleteButton();
                return true;
            //back Arrow Button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DeleteAsyanTask(AddTreatment.this, treatment.getId(), AddTreatment.this).execute();
            }

        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void savebutton() {
        if (edit_title.getText().toString().length() == 0) {
            showToast(getResources().getString(R.string.Empty_title_text));
        } else {
            //
            // Getting Time Will started in it
            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);

            long selectedTimestamp = mCalendar.getTimeInMillis();

            long mRepeatTime = 0;
            switch (repetedMode) {
                case ModeRepetation.MINUTE:
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
                    break;
                case ModeRepetation.Hour:
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;

                    break;
                case ModeRepetation.Day:
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;

                    break;
                case ModeRepetation.Week:
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;

                    break;
                case ModeRepetation.Month:
                    mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;

                    break;
            }

            //values will need it in database
        /*
        mTitle
        mNote
        mDate
        mTime
        mRepeat
        mRepeatNo
        mRepeatType
        mWorking
         */
            mTitle = edit_title.getText().toString().trim();
            treatment.setName(mTitle);
            treatment.setNote(mNote);
            treatment.setDate(mDate);
            treatment.setTime(mTime);
            treatment.setRepeat(mRepeat);
            treatment.setRepeat_no(mRepeatNo);
            treatment.setRepeat_type(mRepeatType);
            treatment.setActive(mWorking);
            new UpdateAsyanTask(this, treatment, this).execute();


            if (treatment.isActive()) {
                if (treatment.isRepeat()) {
                    new AlarmAdapter().RepeatAlarm(getApplicationContext(), selectedTimestamp, treatment.getId(), mRepeatTime);
                } else if (!treatment.isRepeat()) {
                    new AlarmAdapter().addAlarm(getApplicationContext(), selectedTimestamp, treatment.getId());
                }
            } else {
                new AlarmAdapter().deleteAlarm(getApplicationContext(), treatment.getId());
            }


        }
    }

//    private void StyleEditText() {
//        int errorColor;
//        final int version = Build.VERSION.SDK_INT;
//        if (version >= 23) {
//            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.material_color_white);
//        } else {
//            errorColor = getResources().getColor(R.color.material_color_white);
//        }
//        String s = getResources().getString(R.string.Empty_title_text);
//        Drawable icon =
//                getResources().getDrawable(R.drawable.ic_error, null);
//        if (icon != null) {
//            icon.setBounds(0, 0,
//                    icon.getIntrinsicWidth(),
//                    icon.getIntrinsicHeight());
//        }
//        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);
//        spannableStringBuilder.setSpan(foregroundColorSpan, 0, s.length(), 0);
//        edit_title.setError(spannableStringBuilder, icon);
//    }

    ////////////////////////////////////////////

    //Views Clciks
    ///For Preparing Toast
    void showToast(String text) {
        if (msToast != null) {
            msToast.cancel();
        }
        msToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        msToast.show();

    }

    //For Note Section
    public void pickNote(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.Get_Note));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        alert.setPositiveButton(getResources().getString(R.string.ok_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            set_Note.setText(getResources().getString(R.string.Default_note));
                        } else {
                            mNote = input.getText().toString().trim();
                            set_Note.setText(mNote);
                            showToast(getResources().getString(R.string.Notes_Updated));

                        }
                    }
                });
        alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    //For Button OF Switch of ON
    public void turnON(View view) {
        int off = R.drawable.ic_notifications_off_grey600_24dp;
        int on = R.drawable.ic_notifications_on_white_24dp;
        if (mWorking) {
            mWorking = false;
            fab_turn_no_off.setIcon(off);
            showToast(getResources().getString(R.string.Reminder_Truned_off));


        } else {
            mWorking = true;
            fab_turn_no_off.setIcon(on);
            showToast(getResources().getString(R.string.Reminder_Truned_on));

        }

    }

    //For Date Section
    public void ChooseDate(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show(getFragmentManager(), "datePickerDialog");

    }

    //Reult from dialog Date
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //Date get From DatePicker the months - \
        //we need to increase is
        mDay = dayOfMonth;
        monthOfYear++;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        set_date.setText(mDate);
    }

    // For Time Section
    public void AddTime(View view) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.setThemeDark(false);
        timePickerDialog.show(getFragmentManager(), "timePickerDialog");
    }

    //Dialog From Time Section
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        set_time.setText(mTime);
    }

    //For Swithch Section
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = true;
            set_repeat.setText(getResources().getString(R.string.Repetaion) + " " + mRepeatNo + " " + mRepeatType);
        } else {
            mRepeat = false;
            set_repeat.setText(R.string.repeat_off);
        }
    }

    // For Repetation Interval Section
    public void setRepeatNo(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.Get_Number));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton(getResources().getString(R.string.ok_dialog),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            set_repeat_no.setText(mRepeatNo);
                            set_repeat.setText(getResources().getString(R.string.Repetaion) + " " + mRepeatNo + " " + mRepeatType);
                        } else {
                            mRepeatNo = input.getText().toString().trim();
                            set_repeat_no.setText(mRepeatNo);
                            set_repeat.setText(getResources().getString(R.string.Repetaion) + " " + mRepeatNo + " " + mRepeatType);
                        }
                    }
                });
        alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    // For Types OF Repetation Section
    public void selectRepeatType(View view) {
        final String[] itemNames = getResources().getStringArray(R.array.Types_Repetaion);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Select_Type));
        builder.setItems(itemNames, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = itemNames[item];
                repetedMode = item;
                set_repeat_no.setText(mRepeatNo);
                set_repeat.setText(getResources().getString(R.string.Repetaion) + " " + mRepeatNo + " " + mRepeatType);
                set_repeat_type.setText(mRepeatType);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //values will need it in database
        /*
        mTitle
        mNote
        mDate
        mTime
        mRepeat
        mRepeatNo
        mRepeatType
        mWorking
         */

        outState.putString(KEY_TITLE, mTitle);
        outState.putString(KEY_Note, mNote);
        outState.putString(KEY_DATE, mDate);
        outState.putString(KEY_TIME, mTime);
        outState.putString(KEY_REPEAT_NO, mRepeatNo);
        outState.putString(KEY_REPEAT_TYPE, mRepeatType);
        outState.putInt(KEY_Mode_Repatation, repetedMode);
        outState.putBoolean(KEY_REPEAT, mRepeat);
        outState.putBoolean(KEY_Working, mWorking);
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
    public void onUpdateFinshed(List<Treatment> treatmentList) {
        Intent intent = new Intent(AddTreatment.this, MainScreen.class);
        intent.putExtra(MainScreen.Data_FROM_DATABASE, (ArrayList<? extends Parcelable>) treatmentList);
        startActivity(intent);
        TreatmentWidgetService.startActionGetUpdate(this);
        showToast(getResources().getString(R.string.Success_added));
        fa.finish();
        finish();
    }

    @Override
    public void onDeleteFinshed(List<Treatment> treatmentList) {
        Intent intent = new Intent(AddTreatment.this, MainScreen.class);
        intent.putExtra(MainScreen.Data_FROM_DATABASE, (ArrayList<? extends Parcelable>) treatmentList);
        startActivity(intent);
        showToast(getResources().getString(R.string.Treatment_Reminder_deleted));
        //stop This reminder
        new AlarmAdapter().deleteAlarm(getApplicationContext(),treatment.getId());
        //UpdateWidget
        TreatmentWidgetService.startActionGetUpdate(this);
         //Close BackArrow Of MainScreen the ui changed
        fa.finish();
        finish();

    }
}