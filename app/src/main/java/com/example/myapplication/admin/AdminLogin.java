package com.example.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class AdminLogin extends AppCompatActivity {
    private Toolbar toolbar;
    private final int ADMIN_CODE = 1111;
    private Button adminLoginButton;
    private TextInputEditText adminCodeTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);

        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle back arrow click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to the previous activity
            }
        });

        adminLoginButton = findViewById(R.id.AdminLoginButton);
        adminCodeTextInput = findViewById(R.id.adminCodeTextInput);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminCodeStr = adminCodeTextInput.getText().toString().trim();
                if (!adminCodeStr.isEmpty()) {
                    try {
                        int enteredAdminCode = Integer.parseInt(adminCodeStr);
                        if (enteredAdminCode == ADMIN_CODE) {
                            // Admin code is correct, navigate to admin functionality
                            Toast.makeText(AdminLogin.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                            //Replace AdminActivity.class with the correct activity name
                            Intent intent = new Intent(AdminLogin.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdminLogin.this, "Incorrect Admin Code", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(AdminLogin.this, "Invalid Admin Code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminLogin.this, "Please enter Admin Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}