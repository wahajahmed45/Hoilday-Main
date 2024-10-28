package com.example.apparchilog.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.example.apparchilog.R;
import com.example.apparchilog.models.requests.ParticipantFirebase;

import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ParticipantFirebase> participantFirebaseList;
    private OnUserClickListener listener;
    private final Context context;

    public ChatAdapter(Context context, List<ParticipantFirebase> participantFirebaseList) {
        this.participantFirebaseList = participantFirebaseList;
        this.context = context;

    }

    public void addParticipant(ParticipantFirebase participant) {
        if (!participantFirebaseList.contains(participant)) {
            participantFirebaseList.add(participant);
            notifyItemInserted(participantFirebaseList.size() - 1);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(ParticipantFirebase userFirebase);
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUserList(List<ParticipantFirebase> nemUserList) {
        participantFirebaseList.clear();
        participantFirebaseList.addAll(nemUserList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ParticipantFirebase user = participantFirebaseList.get(position);
        holder.textViewName.setText(user.getLastname());
        holder.textViewLastMessage.setText(user.getLastMessage());
        holder.textViewTime.setText(user.getLastMessageTime());
        drawImageProfile(holder, user);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUserClick(user);
                }
            }
        });
    }

    private static void drawImageProfile(@NotNull ChatViewHolder holder, ParticipantFirebase user) {
        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty())
            Glide.with(holder.itemView.getContext())
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .into(holder.imageViewAvatar);
        else {
            // Generate an image with the first letter of the first name
            String firstLetter = String.valueOf(user.getLastname().charAt(0));
            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
            int color = colorGenerator.getColor(user.getLastname());

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            holder.imageViewAvatar.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return participantFirebaseList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewAvatar;
        TextView textViewName;
        TextView textViewLastMessage;
        TextView textViewTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}
