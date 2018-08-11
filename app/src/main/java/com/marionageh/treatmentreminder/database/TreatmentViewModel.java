package com.marionageh.treatmentreminder.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.marionageh.treatmentreminder.LiveDataCustomsClass.DeleteAsyanTask;
import com.marionageh.treatmentreminder.LiveDataCustomsClass.InsertAsyanTask;
import com.marionageh.treatmentreminder.LiveDataCustomsClass.UpdateAsyanTask;
import com.marionageh.treatmentreminder.models.Treatment;

import java.util.List;

public class TreatmentViewModel extends AndroidViewModel {


    private final LiveData<List<Treatment>> treatmentlivedata;

    private AppDatabase appDatabase;


    public TreatmentViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(this.getApplication());
        treatmentlivedata = appDatabase.getTreatmentDao().getAllLive();


    }

    public LiveData<List<Treatment>> getTreatmentlivedata() {
        return treatmentlivedata;
    }

    public void deleteTreatment(int id,DeleteAsyanTask.CustomReminder customReminder) {
        new DeleteAsyanTask(appDatabase, id,customReminder).execute();
    }

    public void updateTreatment(Treatment treatment,UpdateAsyanTask.CustomReminder customReminder) {
        new UpdateAsyanTask(appDatabase, treatment,customReminder).execute();
    }
    public void insertTreatment(Treatment treatment, InsertAsyanTask.CustomReminder customReminder) {
        new InsertAsyanTask(appDatabase, treatment,customReminder).execute();
    }

}
