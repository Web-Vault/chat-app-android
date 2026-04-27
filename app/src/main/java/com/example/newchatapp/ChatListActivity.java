package com.example.newchatapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    ArrayList<ChatUser> userList;
    ChatUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        userList = new ArrayList<>();

        // Dummy users
        userList.add(new ChatUser("Rahul", "Hey bro", "10:30 AM"));
        userList.add(new ChatUser("Priya", "See you soon", "11:15 AM"));
        userList.add(new ChatUser("Aman", "Call me", "Yesterday"));

        adapter = new ChatUserAdapter(this, userList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);
    }
}