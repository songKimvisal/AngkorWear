package com.example.myapplication.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;

public class ResetPassword extends AppCompatActivity {

    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button buttonConfirmReset;
    private DatabaseHelper databaseHelper;
    private String resetToken; // Store the reset token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextNewPassword = findViewById(R.id.resetPasswordNewPassword);
        editTextConfirmPassword = findViewById(R.id.resetPasswordConfirmPassword);
        buttonConfirmReset = findViewById(R.id.resetPasswordConfirmButton);
        databaseHelper = new DatabaseHelper(this);

        // Retrieve the reset token from the Intent
        resetToken = getIntent().getStringExtra("resetToken");

        buttonConfirmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = editTextNewPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ResetPassword.this, "Please enter both passwords", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Verify the reset token and update the password
                    String email = databaseHelper.getEmailFromResetToken(resetToken);
                    if (email != null) {
                        // Update the password
                        boolean passwordUpdated = databaseHelper.updatePassword(email, newPassword);
                        if (passwordUpdated) {
                            // Clear the reset token
                            databaseHelper.clearResetToken(email);
                            Toast.makeText(ResetPassword.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        } else {
                            Toast.makeText(ResetPassword.this, "Password reset failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ResetPassword.this, "Invalid reset token", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}