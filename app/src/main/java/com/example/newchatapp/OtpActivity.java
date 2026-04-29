package com.example.newchatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class OtpActivity extends AppCompatActivity {

    EditText otpBox;
    Button verifyOtpBtn;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpBox = findViewById(R.id.otpBox);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);

        // Get mobile number from LoginActivity
        mobileNumber = getIntent().getStringExtra("mobile");

        verifyOtpBtn.setOnClickListener(v -> {
            String otp = otpBox.getText().toString().trim();

            if (otp.isEmpty()) {
                otpBox.setError("Enter OTP");
                return;
            }

            // Save login status + mobile number
            SharedPreferences preferences =
                    getSharedPreferences("loginPrefs", MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("mobileNumber", mobileNumber);
            editor.apply();

            // Open Chat Home
            Intent intent = new Intent(OtpActivity.this, ChatListActivity.class);
            startActivity(intent);
            finish();
        });
    }
}