package com.example.myapplication.auth;

import android.content.Intent;
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

public class ForgetPassword extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonResetPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEmail = findViewById(R.id.forgetPasswordEmail);
        buttonResetPassword = findViewById(R.id.resetPasswordButton);
        databaseHelper = new DatabaseHelper(this);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgetPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    if(databaseHelper.checkEmailExists(email)) {

                        //Generate Token
                        String resetToken = java.util.UUID.randomUUID().toString();

                        //Store the token
                        databaseHelper.storeResetToken(email, resetToken);

                        //Create intent to start ResetPassword activity
                        Intent intent = new Intent(ForgetPassword.this, ResetPassword.class);
                        intent.putExtra("resetToken", resetToken); //Pass the token to the next activity.
                        startActivity(intent);
                        finish(); //Close the current activity.
                    } else {
                        Toast.makeText(ForgetPassword.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}