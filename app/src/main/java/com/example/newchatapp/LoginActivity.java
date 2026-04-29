package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumber;
    Button sendOtpBtn;

    FirebaseAuth mAuth;

    public static String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = findViewById(R.id.phoneNumber);
        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        mAuth = FirebaseAuth.getInstance();

        sendOtpBtn.setOnClickListener(v -> {

            String number = phoneNumber.getText().toString().trim();

            if (number.isEmpty()) {
                phoneNumber.setError("Enter Mobile Number");
                return;
            }

            // Add country code manually
            String fullNumber = "+91" + number;

            sendVerificationCode(fullNumber);
        });
    }

    private void sendVerificationCode(String number) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(
                        @NonNull com.google.firebase.auth.PhoneAuthCredential credential) {
                }

                @Override
                public void onVerificationFailed(
                        @NonNull FirebaseException e) {

                    Toast.makeText(LoginActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(
                        @NonNull String s,
                        @NonNull PhoneAuthProvider.ForceResendingToken token) {

                    super.onCodeSent(s, token);

                    verificationId = s;

                    Intent intent =
                            new Intent(LoginActivity.this, OtpActivity.class);

                    intent.putExtra("mobile",
                            phoneNumber.getText().toString().trim());

                    startActivity(intent);
                }
            };
}