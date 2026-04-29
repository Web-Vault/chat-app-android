package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    TextView profileName, profileMobile, profileStatus;
    Button logoutBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore database;

    public ProfileFragment() {
        // Required empty public constructor
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
        database = FirebaseFirestore.getInstance();

        loadUserProfile();

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Toast.makeText(getContext(),
                    "Logged out successfully",
                    Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadUserProfile() {

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(),
                    "User not logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        database.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        String name = documentSnapshot.getString("name");
                        String mobile = documentSnapshot.getString("mobileNumber");
                        String status = documentSnapshot.getString("status");

                        profileName.setText("Name: " + name);
                        profileMobile.setText("Mobile: " + mobile);
                        profileStatus.setText("Status: " + status);

                    } else {
                        Toast.makeText(getContext(),
                                "Profile not found",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}