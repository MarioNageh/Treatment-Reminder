package com.marionageh.treatmentreminder.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marionageh.treatmentreminder.R;
import com.marionageh.treatmentreminder.models.Treatment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.ViewHolder> {

    List<Treatment> treatmentList;
    private ListItemClick listItemClick;
    Context context;

    public TreatmentAdapter(List<Treatment> treatmentList, ListItemClick listItemClick, Context context) {
        this.treatmentList = treatmentList;
        this.listItemClick = listItemClick;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(context==null)return;
        //Todo Setup Date Repeat
        int i = position;
        String Date = holder.PrepareDate(treatmentList.get(i).getDate(), treatmentList.get(i).getTime());
        String Reperte = holder.PrepareRepeat(treatmentList.get(i).isRepeat(), treatmentList.get(i).getRepeat_no()
                , treatmentList.get(i).getRepeat_type());
        int image_Id=holder.getImage(treatmentList.get(i).isActive());
        holder.itemView.setTag(treatmentList.get(i).getId());
        holder.PutTextes(treatmentList.get(position).getName(), Date,Reperte,image_Id);
    }

    @Override
    public int getItemCount() {
        return treatmentList == null ? 0 :
                treatmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView treatment_name_text, treatment_date_text, treatment_repeat_text;
        ImageView treatment_iamge_working;

        public ViewHolder(View itemView) {
            super(itemView);
            treatment_name_text =(TextView) itemView.findViewById(R.id.treatment_name_text);
            treatment_date_text = (TextView)itemView.findViewById(R.id.treatment_date_text);
            treatment_repeat_text =(TextView) itemView.findViewById(R.id.treatment_repeat_text);
            treatment_iamge_working = (ImageView)itemView.findViewById(R.id.treatment_iamge_working);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            listItemClick.OnItemClick(getAdapterPosition());
        }

        public String PrepareDate(String date, String time) {
            if(date.equals("")){
                return context.getResources().getString(R.string.No_Date_Time);
            }
            return date + " " + time;
        }

        public String PrepareRepeat(boolean repeat, String repeat_no, String repeat_type) {

            if (repeat){
                return context.getResources().getString(R.string.Repetaion)+" "
                        + repeat_no + " "+ repeat_type;

            }else {
                return context.getResources().getString(R.string.No_Repetation);
            }

        }
        public int getImage(boolean active) {
            if(active){
                return R.drawable.ic_notifitaction;
            }else {
                return R.drawable.ic_notifications_off_grey600_24dp;
            }
        }

        public void PutTextes(String name, String date, String reperte, int image_id) {
            treatment_name_text.setText(name);
            treatment_date_text.setText(date);
            treatment_repeat_text.setText(reperte);
            Picasso.get().load(image_id).into(treatment_iamge_working);
        }
    }

    public void Swapadapter(List<Treatment> treatmentList) {
        if (treatmentList != null) {

            this.treatmentList = treatmentList;
            notifyDataSetChanged();

        }
    }

    public interface ListItemClick {
        void OnItemClick(int Postion);
    }
}
