package com.example.apparchilog.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apparchilog.R;
import com.example.apparchilog.models.holiday.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messageList;
    private final Context mContext;
    private String receiverId;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.messageList = messageList;
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_chat, parent, false);
        return new MessageViewHolder(view);
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /* private boolean isSender(){

     }
     */
    public Message getLastMessage() {
        if(messageList.isEmpty()){
            return null;
        }
        return messageList.get(messageList.size() - 1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        //if (!Objects.equals(currentUser.getEmail(), message.getUserEmail()))
        if (!message.isIssender()) {
            holder.layoutSender.setVisibility(View.VISIBLE);
            holder.layoutReceiver.setVisibility(View.GONE);
            holder.textViewSenderMessage.setText(message.getText());
            holder.textViewSenderTime.setText(message.getFormattedTime());
        } else {
            holder.layoutSender.setVisibility(View.GONE);
            holder.layoutReceiver.setVisibility(View.VISIBLE);
            holder.textViewReceiverMessage.setText(message.getText());
            holder.textViewReceiverTime.setText(message.getFormattedTime());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMessages(List<Message> messages) {
        messageList.clear();
        messageList.addAll(messages);
        notifyDataSetChanged();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutSender;
        TextView textViewSenderMessage;
        TextView textViewSenderTime;
        LinearLayout layoutReceiver;
        TextView textViewReceiverMessage;
        TextView textViewReceiverTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutSender = itemView.findViewById(R.id.layoutSender);
            textViewSenderMessage = itemView.findViewById(R.id.textViewSenderMessage);
            textViewSenderTime = itemView.findViewById(R.id.textViewSenderTime);
            layoutReceiver = itemView.findViewById(R.id.layoutReceiver);
            textViewReceiverMessage = itemView.findViewById(R.id.textViewReceiverMessage);
            textViewReceiverTime = itemView.findViewById(R.id.textViewReceiverTime);
        }
    }
}
