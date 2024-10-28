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
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.utils.DateConvertor;
import com.example.apparchilog.utils.Utils;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private final List<Vacation> vacations;
    private final Context context;
    private OnItemClickListener listener;
    private OnVacationClickListener selectlistener;
    private boolean isDialog;

    public VacationAdapter(Context context, List<Vacation> vacations, boolean isDialog) {
        this.vacations = vacations;
        this.context = context;
        this.isDialog = isDialog;
    }

    public interface OnItemClickListener {
        void onItemClick(Long id);
    }

    public interface OnVacationClickListener {
        void onVacationClick(Vacation vacation);
    }

    public void setOnVacationClickListener(OnVacationClickListener listener) {
        this.selectlistener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Vacation> newVacations) {
        vacations.clear();
        vacations.addAll(newVacations);
        notifyDataSetChanged();
    }

    public void addVacation(Vacation vacation) {
        vacations.add(vacation);
        notifyItemInserted(vacations.size() - 1);
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vacation_layout, parent, false);
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation vacation = vacations.get(position);
        holder.nameTextView.setText(vacation.getName());
        holder.descriptionTextView.setText(vacation.getDescription());
        holder.countryTextView.setText(vacation.getPlaceRequest().getPays());
        String startD = DateConvertor.convertOffsetDateTimeToString(vacation.getStartDate());
        String endD = DateConvertor.convertOffsetDateTimeToString(vacation.getEndDate());
        holder.datesTextView.setText(String.format("%s - %s", startD, endD));
        holder.tvVacationStatus.setText(vacation.getStatus());
        Utils.changeColor(holder.tvVacationStatus, context);
        // Set click listener on the item view
        if (isDialog) {
            holder.itemView.setEnabled(!vacation.getStatus().equals("Passé"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (selectlistener != null) {
                        selectlistener.onVacationClick(vacation);
                    }
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(vacation.getId());
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return vacations.size();
    }

    static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView countryTextView;
        TextView datesTextView;
        TextView tvVacationStatus;

        VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvVacationName);
            descriptionTextView = itemView.findViewById(R.id.tvVacationDescription);
            countryTextView = itemView.findViewById(R.id.tvVacationLocation);
            datesTextView = itemView.findViewById(R.id.tvVacationDates);
            tvVacationStatus = itemView.findViewById(R.id.tvVacationStatus);
        }

    }
}
