package com.example.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.profile.User;

public class EditProfileFragment extends Fragment {

    private EditText editEmail;
    private EditText editPassword;
    private ImageView editProfileImage;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize views
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        editProfileImage = view.findViewById(R.id.edit_profile_image);
        saveButton = view.findViewById(R.id.save_button);
        dbHelper = new DatabaseHelper(requireContext());

        // Retrieve logged-in user email from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", null);

        if (userEmail != null) {
            User user = dbHelper.getUserDetails(userEmail); // Fetch user data from DB

            if (user != null) {

                editProfileImage.setImageResource(R.drawable.profile); // Set default image, can be updated if profile image exists
            }
        }

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            String newEmail = editEmail.getText().toString().trim();
            String newPassword = editPassword.getText().toString().trim();

            if (newEmail.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Update the SharedPreferences with the new email
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_email", newEmail); // Save updated email
                editor.apply();

                // Update the database with the new email and password
                boolean isUpdated = dbHelper.updateUserDetails(newEmail, newPassword);

                if (isUpdated) {
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    // Optionally navigate back to ProfileFragment or MainActivity
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment()) // Go back to ProfileFragment
                            .commit();
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
