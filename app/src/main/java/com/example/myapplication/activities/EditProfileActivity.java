package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private ImageView editProfileImage;
    private DatabaseHelper dbHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize toolbar
        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize views
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editProfileImage = findViewById(R.id.edit_profile_image);
        dbHelper = new DatabaseHelper(this);

        // Handle save button click
        findViewById(R.id.save_button).setOnClickListener(v -> {
            String newEmail = editEmail.getText().toString().trim();
            String newPassword = editPassword.getText().toString().trim();

            if (newEmail.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the database with the new email and password
            boolean isUpdated = dbHelper.updateUserDetails(newEmail, newPassword);

            if (isUpdated) {
                // Save the updated email to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_email", newEmail); // Save updated email
                editor.apply();

                // Send success message back to ProfileFragment
                Intent resultIntent = new Intent();
                resultIntent.putExtra("account_update_message", "Account updated successfully!");
                setResult(RESULT_OK, resultIntent);

                Toast.makeText(this, "Account updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle back arrow click event
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
