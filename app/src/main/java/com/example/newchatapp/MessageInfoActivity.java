package com.example.newchatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MessageInfoActivity
        extends AppCompatActivity {

    TextView txtMessage;
    TextView txtSent;
    TextView txtDelivered;
    TextView txtSeen;

    ImageView btnBack;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_message_info
        );

        txtMessage =
                findViewById(R.id.txtMessage);

        txtSent =
                findViewById(R.id.txtSent);

        txtDelivered =
                findViewById(R.id.txtDelivered);

        txtSeen =
                findViewById(R.id.txtSeen);

        btnBack =
                findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        String message =
                getIntent().getStringExtra("message");

        long sent =
                getIntent().getLongExtra(
                        "sentTime",
                        0
                );

        long delivered =
                getIntent().getLongExtra(
                        "deliveredTime",
                        0
                );

        long seen =
                getIntent().getLongExtra(
                        "seenTime",
                        0
                );

        txtMessage.setText(message);

        txtSent.setText(
                "Sent\n" +
                        getRelativeTime(sent)
        );

        if (delivered > 0) {

            txtDelivered.setText(
                    "Delivered\n" +
                            getRelativeTime(delivered)
            );

        } else {

            txtDelivered.setText(
                    "Delivered\nNot Delivered"
            );
        }

        if (seen > 0) {

            txtSeen.setText(
                    "Seen\n" +
                            getRelativeTime(seen)
            );

        } else {

            txtSeen.setText(
                    "Seen\nNot Seen"
            );
        }
    }

    private String getRelativeTime(long timestamp) {

        long now = System.currentTimeMillis();

        long diff = now - timestamp;

        long minute = 60 * 1000;
        long hour = 60 * minute;
        long day = 24 * hour;

        if (diff < minute) {
            return "Just now";
        }

        if (diff < hour) {

            long minutes = diff / minute;

            return minutes + " min ago";
        }

        if (diff < day) {

            long hours = diff / hour;

            return hours + " hour ago";
        }

        return android.text.format.DateFormat
                .format(
                        "dd MMM yyyy, hh:mm a",
                        timestamp
                )
                .toString();
    }
}