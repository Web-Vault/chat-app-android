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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Views
        chatUserName = findViewById(R.id.chatUserName);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageBox = findViewById(R.id.messageBox);
        sendBtn = findViewById(R.id.sendBtn);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get selected user data from Intent
        receiverName = getIntent().getStringExtra("userName");
        receiverUid = getIntent().getStringExtra("userUid");

        chatUserName.setText(receiverName);

        String senderUid = mAuth.getUid();

        if (senderUid == null || receiverUid == null) {
            finish();
            return;
        }

        // Create sender and receiver rooms
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        // RecyclerView setup
        messageList = new ArrayList<>();
        adapter = new MessageAdapter(this, messageList);

        chatRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        chatRecyclerView.setAdapter(adapter);

        // Load messages in realtime
        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messageList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Message message =
                                    dataSnapshot.getValue(Message.class);

                            if (message != null) {

                                messageList.add(message);

                                // Mark ONLY received messages as seen
                                if (!message.getSenderId().equals(senderUid)) {

                                    String messageKey = dataSnapshot.getKey();

                                    if (messageKey != null) {

                                        // Update receiver room
                                        databaseReference.child("chats")
                                                .child(senderRoom)
                                                .child("messages")
                                                .child(messageKey)
                                                .child("seen")
                                                .setValue(true);

                                        // Update sender room
                                        databaseReference.child("chats")
                                                .child(receiverRoom)
                                                .child("messages")
                                                .child(messageKey)
                                                .child("seen")
                                                .setValue(true);
                                    }
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (messageList.size() > 0) {
                            chatRecyclerView.scrollToPosition(
                                    messageList.size() - 1
                            );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Send message
        sendBtn.setOnClickListener(v -> {

            String messageText =
                    messageBox.getText().toString().trim();

            if (messageText.isEmpty()) {
                return;
            }

            Message message = new Message(
                    messageText,
                    senderUid,
                    System.currentTimeMillis()
            );

            // Save message in sender room
            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .push()
                    .setValue(message);

            // Save message in receiver room
            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .push()
                    .setValue(message);

            // Save last message for sender room
            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("lastMessage")
                    .setValue(messageText);

            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("lastMessageTime")
                    .setValue(System.currentTimeMillis());

            // Save last message for receiver room
            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("lastMessage")
                    .setValue(messageText);

            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("lastMessageTime")
                    .setValue(System.currentTimeMillis());

            // Clear input box
            messageBox.setText("");
        });
    }
}