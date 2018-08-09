package com.marionageh.treatmentreminder.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.marionageh.treatmentreminder.AddTreatment;
import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.customClasses.CustomAsyanTask;
import com.marionageh.treatmentreminder.database.AppDatabase;
import com.marionageh.treatmentreminder.database.TreatmentDao;
import com.marionageh.treatmentreminder.models.Treatment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.marionageh.treatmentreminder.Constants.TREATMENT_LIST;
import static com.marionageh.treatmentreminder.widget.TreatmentServicelist.treatments;

public class TreatmentServicelist extends RemoteViewsService {
    public static List<Treatment> treatments;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


            return new ListRemoteViewsFactory(getApplicationContext());

    }

}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;

    public ListRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {


    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (treatments == null) return 0;
        return treatments.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (treatments.size() == 0) return null;

        int i = position;
        String Date = PrepareDate(treatments.get(i).getDate(), treatments.get(i).getTime());
        String Reperte = PrepareRepeat(treatments.get(i).isRepeat(), treatments.get(i).getRepeat_no()
                , treatments.get(i).getRepeat_type());
        int image_Id=getImage(treatments.get(i).isActive());

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        views.setTextViewText(R.id.treatment_name_text_wid, treatments.get(i).getName());
        views.setTextViewText(R.id.treatment_date_text_wid, Date);
        views.setTextViewText(R.id.treatment_repeat_text_wid, Reperte);
        views.setImageViewResource(R.id.treatment_iamge_working_wid,image_Id);


        Bundle extras = new Bundle();
        extras.putParcelable(AddTreatment.TREAMENT_SENDER, treatments.get(i));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.idslayout, fillInIntent);

        return views;

    }

    public String PrepareDate(String date, String time) {
        if(date.equals("")){
            return mContext.getResources().getString(R.string.No_Date_Time);
        }
        return date + " " + time;
    }

    public String PrepareRepeat(boolean repeat, String repeat_no, String repeat_type) {

        if (repeat){
            return mContext.getResources().getString(R.string.Repetaion)+" "
                    + repeat_no + " "+ repeat_type;

        }else {
            return mContext.getResources().getString(R.string.No_Repetation);
        }

    }
    public int getImage(boolean active) {
        if(active){
            return R.drawable.ic_notifitaction;
        }else {
            return R.drawable.ic_notifications_off_grey600_24dp;
        }
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

