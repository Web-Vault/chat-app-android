package com.example.newchatapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    TextView chatUserName;
    RecyclerView chatRecyclerView;
    EditText messageBox;
    Button sendBtn;

    ArrayList<Message> messageList;
    MessageAdapter adapter;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    String senderRoom;
    String receiverRoom;

    String receiverName;
    String receiverUid;

    ChildEventListener messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatUserName = findViewById(R.id.chatUserName);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageBox = findViewById(R.id.messageBox);
        sendBtn = findViewById(R.id.sendBtn);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        receiverName = getIntent().getStringExtra("userName");
        receiverUid = getIntent().getStringExtra("userUid");

        chatUserName.setText(receiverName);

        String senderUid = mAuth.getUid();

        if (senderUid == null || receiverUid == null) {
            finish();
            return;
        }

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(this, messageList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        // 🔥 SEND MESSAGE
        sendBtn.setOnClickListener(v -> {

            String messageText = messageBox.getText().toString().trim();

            if (messageText.isEmpty()) return;

            String messageId = databaseReference.push().getKey();
            if (messageId == null) return;

            Message message = new Message(
                    messageText,
                    senderUid,
                    System.currentTimeMillis()
            );

            // sender
            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(messageId)
                    .setValue(message);

            // receiver
            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .child(messageId)
                    .setValue(message);

            // last message
            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("lastMessage")
                    .setValue(messageText);

            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("lastMessageTime")
                    .setValue(System.currentTimeMillis());

            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("lastMessage")
                    .setValue(messageText);

            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("lastMessageTime")
                    .setValue(System.currentTimeMillis());

            messageBox.setText("");
        });
    }

    // 🔥 ATTACH LISTENER
    @Override
    protected void onResume() {
        super.onResume();

        attachListener();
    }

    // 🔥 REMOVE LISTENER
    @Override
    protected void onPause() {
        super.onPause();

        if (messageListener != null) {
            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .removeEventListener(messageListener);
        }
    }

    // 🔥 MAIN LISTENER
    private void attachListener() {

        messageListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {

                Message message = snapshot.getValue(Message.class);

                if (message != null) {

                    messageList.add(message);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);

                    String messageKey = snapshot.getKey();

                    if (!message.getSenderId().equals(mAuth.getUid()) && messageKey != null) {

                        // DELIVERED
                        databaseReference.child("chats")
                                .child(senderRoom)
                                .child("messages")
                                .child(messageKey)
                                .child("delivered")
                                .setValue(true);

                        databaseReference.child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(messageKey)
                                .child("delivered")
                                .setValue(true);

                        // SEEN
                        databaseReference.child("chats")
                                .child(senderRoom)
                                .child("messages")
                                .child(messageKey)
                                .child("seen")
                                .setValue(true);

                        databaseReference.child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(messageKey)
                                .child("seen")
                                .setValue(true);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {

                Message updatedMessage = snapshot.getValue(Message.class);

                if (updatedMessage != null) {

                    for (int i = 0; i < messageList.size(); i++) {

                        Message local = messageList.get(i);

                        if (local.getTimestamp() == updatedMessage.getTimestamp()
                                && local.getSenderId().equals(updatedMessage.getSenderId())) {

                            messageList.set(i, updatedMessage);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        };

        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .addChildEventListener(messageListener);
    }
}