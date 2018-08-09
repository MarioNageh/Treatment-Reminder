package com.marionageh.treatmentreminder.customClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CustomAsyanTask extends AsyncTask<Void , Void , List<Treatment>> {

    private CustomAsyncListener listener;
    //WeakReference for no leak of  memory
    private WeakReference<Context> contextWeakReference;

    public CustomAsyanTask(CustomAsyncListener listener, Context contextWeakReference) {
        this.listener = listener;
        this.contextWeakReference = new WeakReference<>(contextWeakReference);
    }

    @Override
    protected List<Treatment> doInBackground(Void... voids) {
        if (contextWeakReference == null){
            return null;
        }

        AppDatabase appDatabase = AppDatabase.getInstance(contextWeakReference.get());
        TreatmentDao treatmentDao = appDatabase.getTreatmentDao();
        List<Treatment> treatmentList = treatmentDao.getAll();

        if (treatmentList == null || treatmentList.size() == 0){
            // If Null Mean that database is null
            //Todo desing layout for non data
            treatmentList = new ArrayList<>();
        }

        return treatmentList;
    }

    @Override
    protected void onPostExecute(List<Treatment> treatmentList) {
        listener.onListReceived(treatmentList);
    }

    // Listener
    public interface CustomAsyncListener {
        void onListReceived(List<Treatment> treatmentList);
    }

}

