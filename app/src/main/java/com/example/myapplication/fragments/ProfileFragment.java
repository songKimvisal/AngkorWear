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
import android.widget.Toast;

import com.example.myapplication.activities.EditProfileActivity;
import com.example.myapplication.auth.Login;
import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.profile.User;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView, emailTextView;
    private DatabaseHelper dbHelper;
    private static final int REQUEST_CODE_EDIT_PROFILE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = view.findViewById(R.id.profile_username);
        emailTextView = view.findViewById(R.id.profile_email);
        dbHelper = new DatabaseHelper(requireContext());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        if (userEmail != null) {
            User user = dbHelper.getUserDetails(userEmail);
            if (user != null) {
                usernameTextView.setText(user.getUsername());
                emailTextView.setText(user.getEmail());
            }
        }

        view.findViewById(R.id.logout_button).setOnClickListener(v -> logout());
        view.findViewById(R.id.edit_profile_button).setOnClickListener(v -> editProfileOnClick());

        return view;
    }

    private void logout() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(requireContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void editProfileOnClick() {
        Intent intent = new Intent(requireContext(), EditProfileActivity.class);
        startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE);  // Use startActivityForResult
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == getActivity().RESULT_OK) {
            // Retrieve the success message from EditProfileActivity
            String message = data.getStringExtra("account_update_message");
            if (message != null) {
                // Show a toast with the success message
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
