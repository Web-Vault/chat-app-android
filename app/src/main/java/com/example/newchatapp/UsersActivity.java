package com.example.newchatapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    ArrayList<ChatUser> userList;
    ChatUserAdapter adapter;

    FirebaseFirestore database;
    FirebaseAuth mAuth;

    ImageView BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        BackBtn = findViewById(R.id.btnBack);

        BackBtn.setOnClickListener(v -> finish());

        userList = new ArrayList<>();
        adapter = new ChatUserAdapter(this, userList);

        usersRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        usersRecyclerView.setAdapter(adapter);

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadUsers();
    }

    private void loadUsers() {

        database.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    userList.clear();

                    for (DocumentSnapshot snapshot :
                            queryDocumentSnapshots.getDocuments()) {

                        ChatUser user = snapshot.toObject(ChatUser.class);

                        if (!user.getUid().equals(mAuth.getUid())) {
                            userList.add(user);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}