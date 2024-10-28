package com.example.apparchilog.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apparchilog.models.holiday.Participant;
import com.example.apparchilog.R;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private final List<Participant> participants;
    private OnDeleteClickListener onDeleteClickListener;
    private final Context context;

    public ParticipantAdapter(Context context, List<Participant> participants) {
        this.context = context;
        this.participants = participants;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeParticipant(Long participantId) {
        if(participants != null) {
            participants.removeIf(participant -> participant.getId().equals(participantId));
            notifyDataSetChanged();
        }
    }


    public interface OnDeleteClickListener {
        void onDeleteClick(Long id_participant);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_participant, parent, false);
        return new ParticipantViewHolder(itemView);
    }
    public void addParticipant(Participant participant) {
        participants.add(participant);
        notifyItemInserted(participants.size() - 1);
    }
    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        Participant participant = participants.get(position);
        holder.tvParticipantName.setText(participant.getNom());
        holder.tvParticipantPrenom.setText(participant.getPrenom());
        holder.btnDeleteParticipant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(participant.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateParticipant(List<Participant> participants) {
        this.participants.clear();
        this.participants.addAll(participants);
        notifyDataSetChanged();
    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvParticipantName;
        private final TextView tvParticipantPrenom;
        private final ImageView btnDeleteParticipant;

        public ParticipantViewHolder(View itemView) {
            super(itemView);
            tvParticipantName = itemView.findViewById(R.id.tvParticipantName);
            tvParticipantPrenom = itemView.findViewById(R.id.tvParticipantPrenom);
            btnDeleteParticipant = itemView.findViewById(R.id.btnDeleteParticipant);
        }
    }
}
