package com.example.apparchilog.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Vacation;
import com.example.apparchilog.utils.DateConvertor;

import java.util.List;

public class VacationDocumentAdapter extends RecyclerView.Adapter<VacationDocumentAdapter.VacationViewHolder> {

    private Context context;
    private List<Vacation> vacations;

    public VacationDocumentAdapter(Context context, List<Vacation> vacations) {
        this.context = context;
        this.vacations = vacations;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vacation_document, parent, false);
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation vacation = vacations.get(position);
        holder.textViewVacationName.setText(vacation.getName());
        String startD = DateConvertor.convertOffsetDateTimeToString(vacation.getStartDate());
        String endD = DateConvertor.convertOffsetDateTimeToString(vacation.getEndDate());
        holder.textViewStartDate.setText(String.format("%s - %s", startD, endD));

        DocumentAdapter documentAdapter = new DocumentAdapter(context, vacation.getDocuments());
        holder.recyclerViewDocuments.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewDocuments.setAdapter(documentAdapter);
    }

    @Override
    public int getItemCount() {
        return vacations.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Vacation> newVacations) {
        vacations.clear();
        vacations.addAll(newVacations);
        notifyDataSetChanged();
    }

    public static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVacationName;
        TextView textViewStartDate;
        RecyclerView recyclerViewDocuments;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVacationName = itemView.findViewById(R.id.textViewVacationName);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            recyclerViewDocuments = itemView.findViewById(R.id.recyclerViewDocuments);
        }
    }
}
