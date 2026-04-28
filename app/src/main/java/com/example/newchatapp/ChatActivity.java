package com.example.newchatapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    TextView chatUserName, backBtn;
    EditText messageBox;
    Button sendBtn;
    RecyclerView messageRecyclerView;

    ArrayList<Message> messageList;
    MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatUserName = findViewById(R.id.chatUserName);
        backBtn = findViewById(R.id.backBtn);
        messageBox = findViewById(R.id.messageBox);
        sendBtn = findViewById(R.id.sendBtn);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);

        String userName = getIntent().getStringExtra("userName");

        if (userName != null) {
            chatUserName.setText(userName);
        } else {
            chatUserName.setText("Chat");
        }

        backBtn.setOnClickListener(v -> finish());

        messageList = new ArrayList<>();

        // Dummy messages
        messageList.add(new Message("Hi bro", "other"));
        messageList.add(new Message("Hello", "me"));
        messageList.add(new Message("Where are you?", "other"));
        messageList.add(new Message("Coming soon", "me"));

        adapter = new MessageAdapter(this, messageList);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(adapter);

        sendBtn.setOnClickListener(v -> {
            String msg = messageBox.getText().toString().trim();

            if (!msg.isEmpty()) {
                messageList.add(new Message(msg, "me"));
                adapter.notifyDataSetChanged();
                messageBox.setText("");
                messageRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            }
        });
    }
}