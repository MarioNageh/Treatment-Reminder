package com.marionageh.treatmentreminder.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.customClasses.CustomAsyanTask;
import com.marionageh.treatmentreminder.models.Treatment;

import java.util.ArrayList;
import java.util.List;

public class TreatmentWidgetService extends IntentService implements CustomAsyanTask.CustomAsyncListener {
    public static final String ACTION_GET_DATA_And_UPDATE =
            "UPDATE_ACTION_GET_DATA";

    public TreatmentWidgetService() {
        super("TreatmentWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_DATA_And_UPDATE.equals(action)) {
                handleActionGetUpdate();
            }
        }

    }

    public static void startActionGetUpdate(Context context) {
        Intent intent = new Intent(context, TreatmentWidgetService.class);
        intent.setAction(ACTION_GET_DATA_And_UPDATE);
        context.startService(intent);
    }


    private void handleActionGetUpdate() {
        new CustomAsyanTask(this, this).execute();
    }


    @Override
    public void onListReceived(List<Treatment> treatmentList) {
        if (treatmentList.size() == 0) {
            //That's Mean That DataBase Empty
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TreatmentWidget.class));
            TreatmentWidget.updateAllWidgetNoData(this, appWidgetManager, appWidgetIds);
            return;

        }
        Log.e("AAAAWWWWWWWWWWWWWW",treatmentList.size()+"");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TreatmentWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_itemes_widget);
        TreatmentWidget.UpdateAllTreatmentWidgets(this, appWidgetManager, treatmentList, appWidgetIds);


    }
}
