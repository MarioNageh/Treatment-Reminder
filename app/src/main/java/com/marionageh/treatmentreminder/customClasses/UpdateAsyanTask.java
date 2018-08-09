package com.marionageh.treatmentreminder.customClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UpdateAsyanTask  extends AsyncTask<Void, Void, List<Treatment>> {


    public static final String DONE = "Done";
    private WeakReference<Context> contextWeakReference;
    private Treatment treatment;
    UpdateAsyanTask.CustomReminder customReminder;
    List<Treatment> treatmentList = new ArrayList<>();

    public UpdateAsyanTask(Context contextWeakReference, Treatment treatment, UpdateAsyanTask.CustomReminder customReminder) {
        this.contextWeakReference = new WeakReference<>(contextWeakReference);
        this.treatment = treatment;
        this.customReminder = customReminder;
    }

    @Override
    protected List<Treatment> doInBackground(Void... voids) {
        if (contextWeakReference != null) {

            AppDatabase appDatabase = AppDatabase.getInstance(contextWeakReference.get());
            TreatmentDao treatmentDao = appDatabase.getTreatmentDao();
            treatmentDao.update(treatment);
            treatmentList = treatmentDao.getAll();
            return treatmentList;
        }

        return treatmentList;
    }

    @Override
    protected void onPostExecute(List<Treatment> treatmentList) {
        customReminder.onUpdateFinshed(treatmentList);
    }

    // Listener
    public interface CustomReminder {
        void onUpdateFinshed(List<Treatment> treatmentList);
    }
}

