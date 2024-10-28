package com.example.apparchilog.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Activity;
import com.example.apparchilog.utils.DateConvertor;
import com.example.apparchilog.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private final List<Activity> activities;
    private final Context context;
    private OnItemClickListener listener;

    public ActivityAdapter(Context context, ArrayList<Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_activity_layout, parent, false);
        return new ActivityViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = activities.get(position);
        holder.tvActivityName.setText(activity.getNom());
        holder.tvActivityDescription.setText(activity.getDescription());
        String startD = DateConvertor.convertOffsetDateTimeToString(activity.getDateDebut());
        String endD = DateConvertor.convertOffsetDateTimeToString(activity.getDateFin());
        holder.tvActivityPeriod.setText(startD + " - " + endD);
        holder.tvActivityPlace.setText(String.format("%s - %s", activity.getPlaceRequest().getPays(), activity.getPlaceRequest().getVille()));
        holder.tvActivityStatus.setText(activity.getStatus()!= null?activity.getStatus():"non");
        Utils.changeColor(holder.tvActivityStatus, context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(activity.getId());
                }
            }
        });
    }

    public Long getVacationId(Long idActivity) {
        List<Activity> activities = this.activities;
        if(!activities.isEmpty()){
            for(Activity activity : activities){
                if(activity.getId().equals(idActivity)){
                    return activity.getVacationId();
                }
            }
        }
        return 0L;
    }

    public interface OnItemClickListener {
        void onItemClick(Long id_activity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addActivity(Activity activity) {
        activities.add(activity);
        notifyItemInserted(activities.size() - 1);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateActivities(List<Activity> activities) {
        this.activities.clear();
        this.activities.addAll(activities);
        notifyDataSetChanged();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvActivityName;
        private final TextView tvActivityDescription;
        private final TextView tvActivityPeriod;
        private final TextView tvActivityPlace;
        private final TextView tvActivityStatus;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvActivityDescription = itemView.findViewById(R.id.tvActivityDescription);
            tvActivityPeriod = itemView.findViewById(R.id.tvActivityPeriod);
            tvActivityPlace = itemView.findViewById(R.id.tvActivityPlace);
            tvActivityStatus = itemView.findViewById(R.id.tvActivityStatus);
        }
    }
}
