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
import com.example.myapplication.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class AdminLogin extends AppCompatActivity {
    private Toolbar toolbar;
    private Button adminLoginButton;
    private TextInputEditText adminEmailTextInput;
    private TextInputEditText adminPasswordTextInput;
    private DatabaseHelper dbHelper;

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
        adminEmailTextInput = findViewById(R.id.adminEmailTextInput);
        adminPasswordTextInput = findViewById(R.id.adminPasswordTextInput);
        dbHelper = new DatabaseHelper(this);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = adminEmailTextInput.getText().toString().trim();
                String password = adminPasswordTextInput.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    // Use the specific email and password you want to check.
                    if (email.equals("A") && password.equals("1")) {
                        Toast.makeText(AdminLogin.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminLogin.this, AdminPanelActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AdminLogin.this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminLogin.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}