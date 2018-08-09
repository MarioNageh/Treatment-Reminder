package com.marionageh.treatmentreminder.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.marionageh.treatmentreminder.AddTreatment;
import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.models.Treatment;
import com.marionageh.treatmentreminder.splash_screen;

import java.util.List;

import static com.marionageh.treatmentreminder.Constants.TREATMENT_LIST;

/**
 * Implementation of App Widget functionality.
 */
public class TreatmentWidget extends AppWidgetProvider {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        TreatmentWidgetService.startActionGetUpdate(context);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateAllWidgetNoData(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidgetNoData(context, appWidgetManager, appWidgetId);

        }
    }

    private static void updateAppWidgetNoData(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.no_data_layout);
        Intent appIntent = new Intent(context, splash_screen.class);
        appIntent.setAction(TreatmentWidgetService.ACTION_GET_DATA_And_UPDATE);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.layout_ids, appPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void UpdateAllTreatmentWidgets(Context context, AppWidgetManager appWidgetManager,
                                                 List<Treatment> treatmentList, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, treatmentList);
            Log.e("MARIOOOO","AAAAAAAAAA");

        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, List<Treatment> treatmentList) {
        Log.e("AAAAWWWWWWWWWWWWWW",treatmentList.size()+"");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.treatment_widget_list);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, TreatmentServicelist.class);

        TreatmentServicelist.treatments=treatmentList;
        views.setRemoteAdapter(R.id.list_itemes_widget, intent);
        // Set the PlantDetailActivity intent to launch when clicked
            Intent appIntent = new Intent(context, AddTreatment.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list_itemes_widget, appPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

}


