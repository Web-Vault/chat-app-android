package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // User already logged in
            Intent intent = new Intent(
                    SplashActivity.this,
                    ChatListActivity.class
            );
            startActivity(intent);
            finish();

        } else {
            // New user → Login screen
            Intent intent = new Intent(
                    SplashActivity.this,
                    LoginActivity.class
            );
            startActivity(intent);
            finish();
        }
    }
}