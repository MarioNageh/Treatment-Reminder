package com.marionageh.treatmentreminder.LiveDataCustomsClass;

import android.content.Context;
import android.os.AsyncTask;

import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DeleteAsyanTask extends AsyncTask<Void, Void, List<Treatment>> {


    private AppDatabase db;
    private int id;
    CustomReminder customReminder;

    public DeleteAsyanTask(AppDatabase db, int id, CustomReminder customReminder) {
        this.db = db;
        this.id = id;
        this.customReminder = customReminder;
    }

    @Override
    protected  List<Treatment> doInBackground(Void... voids) {
        db.getTreatmentDao().deleteByUserId(id);
        return db.getTreatmentDao().getAll();
    }



    @Override
    protected void onPostExecute(List<Treatment> treatmentList) {
        customReminder.onDeleteFinshed(treatmentList);
    }

    // Listener
    public interface CustomReminder {
        void onDeleteFinshed(List<Treatment> treatmentList);
    }
}
