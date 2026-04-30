package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class OtpActivity extends AppCompatActivity {

    EditText otpBox;
    Button verifyOtpBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore database;

    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpBox = findViewById(R.id.otpBox);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        mobileNumber = getIntent().getStringExtra("mobile");

        verifyOtpBtn.setOnClickListener(v -> {

            String otp = otpBox.getText().toString().trim();

            if (otp.isEmpty()) {
                otpBox.setError("Enter OTP");
                return;
            }

            verifyCode(otp);
        });
    }

    private void verifyCode(String code) {

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(
                        LoginActivity.verificationId,
                        code
                );

        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        checkUserProfile();

                    } else {

                        Toast.makeText(this,
                                "Invalid OTP",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserProfile() {

        String uid = mAuth.getCurrentUser().getUid();

        database.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        String name = documentSnapshot.getString("name");
                        String mobile = documentSnapshot.getString("mobileNumber");
                        String status = documentSnapshot.getString("status");

                        getSharedPreferences("userPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("name", name)
                                .putString("mobile", mobile)
                                .putString("status", status)
                                .apply();

                        Intent intent = new Intent(
                                OtpActivity.this,
                                ChatListActivity.class
                        );

                        startActivity(intent);
                        finish();
                    } else {
                        // New user → setup profile

                        Intent intent = new Intent(
                                OtpActivity.this,
                                SetupProfileActivity.class
                        );

                        intent.putExtra("mobile", mobileNumber);

                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}