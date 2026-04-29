package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetupProfileActivity extends AppCompatActivity {

    EditText nameBox;
    Button saveProfileBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore database;

    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        nameBox = findViewById(R.id.nameBox);
        saveProfileBtn = findViewById(R.id.saveProfileBtn);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        mobileNumber = getIntent().getStringExtra("mobile");

        saveProfileBtn.setOnClickListener(v -> {

            String name = nameBox.getText().toString().trim();

            if (name.isEmpty()) {
                nameBox.setError("Enter Your Name");
                return;
            }

            saveUserToFirestore(name);
        });
    }

    private void saveUserToFirestore(String name) {

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        User user = new User(
                uid,
                name,
                mobileNumber,
                "",
                "Hey there! I am using Chat App",
                "Online"
        );

        database.collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(unused -> {

                    // Save locally in SharedPreferences
                    getSharedPreferences("userPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("name", name)
                            .putString("mobile", mobileNumber)
                            .putString("status", "Hey there! I am using Chat App")
                            .apply();

                    Toast.makeText(this,
                            "Profile Saved Successfully",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(
                            SetupProfileActivity.this,
                            ChatListActivity.class
                    );

                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            "Failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}