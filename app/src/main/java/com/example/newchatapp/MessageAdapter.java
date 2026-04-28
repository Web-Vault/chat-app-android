package com.example.newchatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Message> messageList;

    int ITEM_SENT = 1;
    int ITEM_RECEIVED = 2;

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSender().equals("me")) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_sent_message, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_received_message, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messageList.get(position);

        if (holder.getClass() == SentViewHolder.class) {
            ((SentViewHolder) holder).sentMessageText.setText(message.getMessage());
        } else {
            ((ReceivedViewHolder) holder).receivedMessageText.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView sentMessageText;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessageText = itemView.findViewById(R.id.sentMessageText);
        }
    }

    public class ReceivedViewHolder extends RecyclerView.ViewHolder {

        TextView receivedMessageText;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessageText = itemView.findViewById(R.id.receivedMessageText);
        }
    }
}