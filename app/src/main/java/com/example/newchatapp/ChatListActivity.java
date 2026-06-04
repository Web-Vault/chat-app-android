package com.example.newchatapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatListActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Default fragment
        loadFragment(new MessagesFragment());

        bottomNavigation.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.messages) {
                selectedFragment = new MessagesFragment();
            }
            else if (item.getItemId() == R.id.calls) {
                selectedFragment = new CallsFragment();
            }
            else if (item.getItemId() == R.id.profile) {
                selectedFragment = new ProfileFragment();
            }

            return loadFragment(selectedFragment);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String uid = mAuth.getUid();

        if (uid != null) {

            firestore.collection("users")
                    .document(uid)
                    .update(
                            "isOnline", true
                    );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        String uid = mAuth.getUid();

        if (uid != null) {

            firestore.collection("users")
                    .document(uid)
                    .update(
                            "isOnline", false,
                            "lastSeen", System.currentTimeMillis()
                    );
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}