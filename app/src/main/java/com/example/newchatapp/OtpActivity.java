package com.example.newchatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {

    EditText otpBox;
    Button verifyOtpBtn;

    FirebaseAuth mAuth;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpBox = findViewById(R.id.otpBox);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);

        mAuth = FirebaseAuth.getInstance();

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

                        SharedPreferences preferences =
                                getSharedPreferences("loginPrefs", MODE_PRIVATE);

                        SharedPreferences.Editor editor =
                                preferences.edit();

                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("mobileNumber", mobileNumber);
                        editor.apply();

                        Intent intent = new Intent(OtpActivity.this, SetupProfileActivity.class);
                        intent.putExtra("mobile", mobileNumber);
                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(this,
                                "Invalid OTP",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}