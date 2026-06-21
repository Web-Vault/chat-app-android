package com.example.newchatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messageList;

    OnMessageLongClickListener listener;

    public interface OnMessageLongClickListener {
        void onMessageLongClick(Message message);
    }

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public MessageAdapter(
            Context context,
            ArrayList<Message> messageList,
            OnMessageLongClickListener listener) {

        this.context = context;
        this.messageList = messageList;
        this.listener = listener;
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

            SentViewHolder viewHolder = (SentViewHolder) holder;

            if (message.isDeleted()) {

                viewHolder.sentMessage.setText(
                        "This message was deleted"
                );

            } else {

                viewHolder.sentMessage.setText(
                        message.getMessage()
                );

                viewHolder.messageTime.setText(
                        formatTime(message.getTimestamp())
                );

                if (message.isReply()) {

                    viewHolder.replyContainer.setVisibility(View.VISIBLE);

                    viewHolder.replySender.setText(
                            message.getReplySenderName()
                    );

                    viewHolder.replyText.setText(
                            message.getReplyMessage()
                    );

                } else {

                    viewHolder.replyContainer.setVisibility(View.GONE);

                }
            }

            holder.itemView.setOnLongClickListener(v -> {

                if (listener != null) {
                    listener.onMessageLongClick(message);
                }

                return true;
            });

            // ✓ / ✓✓ logic
            if (message.isSeen()) {
                viewHolder.messageStatus.setText("✓✓");
                viewHolder.messageStatus.setTextColor(
                        android.graphics.Color.parseColor("#34B7F1")
                );

            } else if (message.isDelivered()) {
                viewHolder.messageStatus.setText("✓✓");
                viewHolder.messageStatus.setTextColor(
                        android.graphics.Color.GRAY
                );

            } else {
                viewHolder.messageStatus.setText("✓");
                viewHolder.messageStatus.setTextColor(
                        android.graphics.Color.GRAY
                );
            }

        }
        // RECEIVED MESSAGE
        else {

            ReceiverViewHolder viewHolder =
                    (ReceiverViewHolder) holder;

            if (message.isDeleted()) {

                viewHolder.receivedMessage.setText(
                        "This message was deleted"
                );

            } else {

                viewHolder.receivedMessage.setText(
                        message.getMessage()
                );

                viewHolder.messageTime.setText(
                        formatTime(message.getTimestamp())
                );

                if (message.isReply()) {

                    viewHolder.replyContainer.setVisibility(View.VISIBLE);

                    if (FirebaseAuth.getInstance().getUid()
                            .equals(message.getReplySenderId())) {

                        viewHolder.replySender.setText("You");

                    } else {

                        viewHolder.replySender.setText(
                                message.getReplySenderName()
                        );
                    }

                    viewHolder.replyText.setText(
                            message.getReplyMessage()
                    );

                } else {

                    viewHolder.replyContainer.setVisibility(View.GONE);

                }
            }

            holder.itemView.setOnLongClickListener(v -> {

                if (listener != null) {
                    listener.onMessageLongClick(message);
                }

                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private String formatTime(long timestamp) {

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat(
                        "hh:mm a",
                        java.util.Locale.getDefault()
                );

        return sdf.format(
                new java.util.Date(timestamp)
        );
    }

    // SENT VIEW HOLDER
    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView sentMessage;
        TextView messageStatus;
        TextView messageTime;

        LinearLayout replyContainer;
        TextView replySender;
        TextView replyText;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);

            sentMessage = itemView.findViewById(R.id.sentMessage);
            messageStatus = itemView.findViewById(R.id.messageStatus);
            replyContainer = itemView.findViewById(R.id.replyContainer);
            replySender = itemView.findViewById(R.id.replySender);
            replyText = itemView.findViewById(R.id.replyText);
            messageTime =
                    itemView.findViewById(R.id.messageTime);
        }
    }

    // RECEIVED VIEW HOLDER
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receivedMessage;
        TextView messageTime;

        LinearLayout replyContainer;
        TextView replySender;
        TextView replyText;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receivedMessage =
                    itemView.findViewById(R.id.receivedMessageText);

            replyContainer =
                    itemView.findViewById(R.id.replyContainer);

            replySender =
                    itemView.findViewById(R.id.replySender);

            replyText =
                    itemView.findViewById(R.id.replyText);

            messageTime =
                    itemView.findViewById(R.id.messageTime);
        }
    }
}