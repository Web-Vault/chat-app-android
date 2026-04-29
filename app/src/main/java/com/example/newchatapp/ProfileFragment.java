package com.example.newchatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    TextView profileName, profileMobile, profileStatus;
    Button logoutBtn;

    FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        profileMobile = view.findViewById(R.id.profileMobile);
        profileStatus = view.findViewById(R.id.profileStatus);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        mAuth = FirebaseAuth.getInstance();

        loadProfileFromLocal();

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();

            // Clear SharedPreferences also
            getActivity()
                    .getSharedPreferences("userPrefs", getContext().MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Toast.makeText(getContext(),
                    "Logged out successfully",
                    Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadProfileFromLocal() {

        SharedPreferences preferences =
                requireActivity().getSharedPreferences("userPrefs", getContext().MODE_PRIVATE);

        String name = preferences.getString("name", "");
        String mobile = preferences.getString("mobile", "");
        String status = preferences.getString("status", "");

        if (!TextUtils.isEmpty(name)) {
            profileName.setText("Name: " + name);
        }

        if (!TextUtils.isEmpty(mobile)) {
            profileMobile.setText("Mobile: " + mobile);
        }

        if (!TextUtils.isEmpty(status)) {
            profileStatus.setText("Status: " + status);
        }
    }
}