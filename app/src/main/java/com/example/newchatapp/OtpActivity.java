package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class OtpActivity extends AppCompatActivity{

    EditText otpBox;
    Button verifyOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpBox = findViewById(R.id.otpBox);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);

        verifyOtpBtn.setOnClickListener(v -> {
            String otp = otpBox.getText().toString().trim();

            if (otp.isEmpty()) {
                otpBox.setError("Enter OTP");
                return;
            }

            Intent intent = new Intent(OtpActivity.this, ChatListActivity.class);
            startActivity(intent);
            finish();
        });
    }



}
