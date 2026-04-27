package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumber;
    Button sendOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = findViewById(R.id.phoneNumber);
        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        sendOtpBtn.setOnClickListener(v -> {
            String number = phoneNumber.getText().toString().trim();

            if(number.isEmpty()) {
                phoneNumber.setError("Enter Mobile Number");
                return;
            }

            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
            intent.putExtra("mobile", number);
            startActivity(intent);
        });
    }
}
