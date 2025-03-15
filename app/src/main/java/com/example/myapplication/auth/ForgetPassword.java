package com.example.myapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;

import java.util.UUID;

public class ForgetPassword extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonResetPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        editTextEmail = findViewById(R.id.forgetPasswordEmail);
        buttonResetPassword = findViewById(R.id.resetPasswordButton);
        databaseHelper = new DatabaseHelper(this);

        buttonResetPassword.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(ForgetPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper.checkEmailExists(email)) {
                // Generate Token
                String resetToken = UUID.randomUUID().toString();

                // Store the token in the database
                databaseHelper.storeResetToken(email, resetToken);

                // Pass email & token to ResetPassword activity
                Intent intent = new Intent(ForgetPassword.this, ResetPassword.class);
                intent.putExtra("email", email);
                intent.putExtra("resetToken", resetToken);
                startActivity(intent);
                finish(); // Close ForgetPassword activity
            } else {
                Toast.makeText(ForgetPassword.this, "Email not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
