package com.example.newchatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messageList;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if (FirebaseAuth.getInstance().getUid()
                .equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_sent_message, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_received_message, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position) {

        Message message = messageList.get(position);

        // SENT MESSAGE
        if (holder instanceof SentViewHolder) {

            SentViewHolder viewHolder =
                    (SentViewHolder) holder;

            viewHolder.sentMessage.setText(
                    message.getMessage()
            );

            // ✓ and ✓✓ logic
            if (message.isSeen()) {
                viewHolder.messageStatus.setText("✓✓");
            } else {
                viewHolder.messageStatus.setText("✓");
            }

        }
        // RECEIVED MESSAGE
        else {

            ReceiverViewHolder viewHolder =
                    (ReceiverViewHolder) holder;

            viewHolder.receivedMessage.setText(
                    message.getMessage()
            );
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // SENT VIEW HOLDER
    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView sentMessage;
        TextView messageStatus;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);

            sentMessage =
                    itemView.findViewById(R.id.sentMessage);

            messageStatus =
                    itemView.findViewById(R.id.messageStatus);
        }
    }

    // RECEIVED VIEW HOLDER
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receivedMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receivedMessage =
                    itemView.findViewById(R.id.receivedMessageText);
        }
    }
}