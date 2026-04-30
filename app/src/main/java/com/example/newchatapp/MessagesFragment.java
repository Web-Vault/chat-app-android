package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    RecyclerView chatRecyclerView;
    FloatingActionButton newChatBtn;

    ArrayList<ChatUser> userList;
    ChatUserAdapter adapter;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    DatabaseReference realtimeDb;

    public MessagesFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_messages,
                container,
                false
        );

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        newChatBtn = view.findViewById(R.id.newChatBtn);

        userList = new ArrayList<>();
        adapter = new ChatUserAdapter(getContext(), userList);

        chatRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
        chatRecyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        realtimeDb = FirebaseDatabase.getInstance().getReference();

        loadRecentChats();

        newChatBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    getActivity(),
                    UsersActivity.class
            );
            startActivity(intent);
        });

        return view;
    }

    private void loadRecentChats() {

        String currentUid = mAuth.getUid();

        if (currentUid == null) return;

        firestore.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    userList.clear();

                    for (DocumentSnapshot snapshot :
                            queryDocumentSnapshots.getDocuments()) {

                        ChatUser user = snapshot.toObject(ChatUser.class);

                        if (user == null) continue;

                        if (user.getUid().equals(currentUid)) {
                            continue;
                        }

                        String senderRoom =
                                currentUid + user.getUid();

                        realtimeDb.child("chats")
                                .child(senderRoom)
                                .addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(
                                                    @NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {

                                                    String lastMessage =
                                                            snapshot.child("lastMessage")
                                                                    .getValue(String.class);

                                                    if (lastMessage != null) {

                                                        user.setLastMessage(lastMessage);
                                                        Long lastMessageTime =
                                                                snapshot.child("lastMessageTime")
                                                                        .getValue(Long.class);

                                                        if (lastMessageTime != null) {
                                                            user.setTime(formatTime(lastMessageTime));
                                                        }

                                                        boolean exists = false;

                                                        for (ChatUser existingUser : userList) {
                                                            if (existingUser.getUid()
                                                                    .equals(user.getUid())) {
                                                                existingUser.setLastMessage(lastMessage);
                                                                exists = true;
                                                                break;
                                                            }
                                                        }

                                                        if (!exists) {
                                                            userList.add(user);
                                                        }

                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(
                                                    @NonNull DatabaseError error) {
                                            }
                                        });
                    }
                });
    }

    private String formatTime(long timestamp) {

        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (minutes < 1) {
            return "Now";
        } else if (minutes < 60) {
            return minutes + " min ago";
        } else if (hours < 24) {
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("hh:mm a");
            return sdf.format(new java.util.Date(timestamp));
        } else if (days == 1) {
            return "Yesterday";
        } else {
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("dd/MM/yy");

            return sdf.format(new java.util.Date(timestamp));
        }
    }
}