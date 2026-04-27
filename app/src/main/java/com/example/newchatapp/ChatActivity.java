package com.example.newchatapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    TextView chatUserName;
    TextView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatUserName = findViewById(R.id.chatUserName);
        backBtn = findViewById(R.id.backBtn);

        String userName = getIntent().getStringExtra("userName");

        if (userName != null) {
            chatUserName.setText(userName);
        } else {
            chatUserName.setText("Chat");
        }

        backBtn.setOnClickListener(v -> finish());
    }
}