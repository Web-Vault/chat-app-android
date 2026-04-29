package com.example.newchatapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    TextView profileName, profileMobile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        profileMobile = view.findViewById(R.id.profileMobile);

        SharedPreferences preferences =
                requireActivity().getSharedPreferences("loginPrefs", getContext().MODE_PRIVATE);

        String mobile =
                preferences.getString("mobileNumber", "Not Available");

        profileName.setText("Name: User");
        profileMobile.setText("Mobile: " + mobile);

        return view;
    }
}