package com.marionageh.treatmentreminder.LiveDataCustomsClass;

import android.content.Context;
import android.os.AsyncTask;

import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UpdateAsyanTask extends AsyncTask<Void, Void, List<Treatment>> {

    private AppDatabase db;
    private Treatment treatment;
    CustomReminder customReminder;

    public UpdateAsyanTask(AppDatabase db, Treatment treatment, CustomReminder customReminder) {
        this.db = db;
        this.treatment = treatment;
        this.customReminder = customReminder;
    }

    @Override
    protected List<Treatment> doInBackground(Void... voids) {

        db.getTreatmentDao().update(treatment);
        return db.getTreatmentDao().getAll();
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

