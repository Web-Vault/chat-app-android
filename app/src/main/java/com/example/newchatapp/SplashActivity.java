package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseRemoteConfig remoteConfig;

    private static final int MODE_NORMAL = 0;
    private static final int MODE_MAINTENANCE = 1;
    private static final int MODE_WIPE_DATA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings settings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(0) // change to 3600 later
                        .build();

        remoteConfig.setConfigSettingsAsync(settings);

        remoteConfig.fetchAndActivate()
                .addOnSuccessListener(unused -> {

                    int appMode =
                            (int) remoteConfig.getLong("app_mode");

                    switch (appMode) {

                        case MODE_MAINTENANCE:
                        case MODE_WIPE_DATA:

                            handleAppMode(appMode);
                            break;

                        case MODE_NORMAL:
                        default:

                            openNextScreen();
                            break;
                    }
                })
                .addOnFailureListener(e -> {

                    // If Remote Config fails,
                    // continue normally

                    openNextScreen();
                });
    }

    private void openNextScreen() {

        if (mAuth.getCurrentUser() != null) {

            Intent intent =
                    new Intent(
                            SplashActivity.this,
                            ChatListActivity.class
                    );

            startActivity(intent);
            finish();

        } else {

            Intent intent =
                    new Intent(
                            SplashActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);
            finish();
        }
    }

    private void handleAppMode(int appMode) {

        String title;
        String message;

        if (appMode == MODE_MAINTENANCE) {

            title = "Maintenance";

            message =
                    "Server is under maintenance.\n\n" +
                            "Please try again later.";

        } else {

            title = "Application Reset";

            message =
                    "Application data is being reset.\n\n" +
                            "Please contact administrator.";
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(
                        "Close App",
                        (dialog, which) -> finishAffinity()
                )
                .show();
    }
}