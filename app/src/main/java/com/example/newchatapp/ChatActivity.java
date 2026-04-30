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

    String receiverName = "User";
    String receiverUid = "demoUser123"; // temporary for testing

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
        chatUserName.setText(receiverName);

        String senderUid = mAuth.getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(this, messageList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        // Load messages realtime
        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            messageList.add(message);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        // Send message
        sendBtn.setOnClickListener(v -> {
            String messageText = messageBox.getText().toString().trim();

            if (messageText.isEmpty()) return;

            Message message = new Message(
                    messageText,
                    senderUid,
                    System.currentTimeMillis()
            );

            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .push()
                    .setValue(message);

            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .push()
                    .setValue(message);

            messageBox.setText("");
        });
    }
}