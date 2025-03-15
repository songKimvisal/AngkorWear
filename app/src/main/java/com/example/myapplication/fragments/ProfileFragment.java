package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.auth.Login;
import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.profile.User;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView, emailTextView;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.profile_username); // Ensure correct ID
        emailTextView = view.findViewById(R.id.profile_email); // Ensure correct ID
        dbHelper = new DatabaseHelper(requireContext());

        // Retrieve logged-in user email from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        if (userEmail != null) {
            // Fetch user data from database
            User user = dbHelper.getUserDetails(userEmail);

            if (user != null) {
                // Set user details in the profile view
                usernameTextView.setText(user.getUsername());
                emailTextView.setText(user.getEmail());
            } else {
                // Handle case if user data is null (e.g., if data isn't found in the database)
                usernameTextView.setText("User not found");
                emailTextView.setText("No email available");
            }
        } else {
            // Handle case if no user email is found in SharedPreferences
            usernameTextView.setText("No user logged in");
            emailTextView.setText("No email available");
        }

        // Handle logout
        view.findViewById(R.id.logout_button).setOnClickListener(v -> logout());

        return view;
    }

    private void logout() {
        // Clear user session
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all stored data in SharedPreferences
        editor.apply();

        // Redirect to Login activity
        Intent intent = new Intent(requireContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
