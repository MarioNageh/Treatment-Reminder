package com.marionageh.treatmentreminder.customClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SelectAsyanTaskByID extends AsyncTask<Void, Void, Treatment> {


    public static final String DONE = "Done";
    private WeakReference<Context> contextWeakReference;
    private int id;
    CustomReminder customReminder;

    public SelectAsyanTaskByID(Context contextWeakReference, int id, CustomReminder customReminder) {
        this.contextWeakReference = new WeakReference<>(contextWeakReference);
        this.id = id;
        this.customReminder = customReminder;
    }

    @Override
    protected Treatment doInBackground(Void... voids) {
        if (contextWeakReference != null) {

            AppDatabase appDatabase = AppDatabase.getInstance(contextWeakReference.get());
            TreatmentDao treatmentDao = appDatabase.getTreatmentDao();
            Treatment treatment =  treatmentDao.SelectTreatmentId(id);
            return treatment;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Treatment treatment) {
        customReminder.onSelectFinshed(treatment);
    }

    // Listener
    public interface CustomReminder {
        void onSelectFinshed(Treatment treatment);
    }
}
