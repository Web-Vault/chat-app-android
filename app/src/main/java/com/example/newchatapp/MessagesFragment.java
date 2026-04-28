// MessagesFragment.java

package com.example.newchatapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    RecyclerView messagesRecyclerView;
    ArrayList<ChatUser> userList;
    ChatUserAdapter adapter;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);

        // Dummy data list
        userList = new ArrayList<>();

        userList.add(new ChatUser("Rahul", "Hey bro", "10:30 AM"));
        userList.add(new ChatUser("Priya", "See you soon", "11:15 AM"));
        userList.add(new ChatUser("Aman", "Call me", "Yesterday"));

        // Adapter setup
        adapter = new ChatUserAdapter(getContext(), userList);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesRecyclerView.setAdapter(adapter);

        return view;
    }
}