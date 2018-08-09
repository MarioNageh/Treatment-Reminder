
package com.marionageh.treatmentreminder.customClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import java.lang.ref.WeakReference;


public class DeleteAsyanTask extends AsyncTask<Void, Void, List<Treatment>> {


    public static final String DONE="Done";
    private WeakReference<Context> contextWeakReference;
    private int id;
    CustomReminder customReminder;
    List<Treatment> treatmentList=new ArrayList<>();


    public DeleteAsyanTask(Context contextWeakReference, int id,CustomReminder customReminder) {
        this.contextWeakReference = new WeakReference<>(contextWeakReference);
        this.id = id;
        this.customReminder=customReminder;
    }

    @Override
    protected     List<Treatment> doInBackground(Void... voids) {
        if (contextWeakReference != null) {

            AppDatabase appDatabase = AppDatabase.getInstance(contextWeakReference.get());
            TreatmentDao treatmentDao = appDatabase.getTreatmentDao();
            treatmentDao.deleteByUserId(id);
          treatmentList = treatmentDao.getAll();
            return treatmentList;
        }

        return treatmentList;
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
