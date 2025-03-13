package com.example.myapplication.loginAndRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dataBase.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {
    private TextInputEditText textUsername, textEmail, textPassword, textPhone;
    private Button RegisterBtn, goTologinBtn;
    private DatabaseHelper dbHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Your register layout

        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        textUsername = findViewById(R.id.textUsername);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        textPhone = findViewById(R.id.textPhone);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        goTologinBtn = findViewById(R.id.goTologinBtn);
        dbHelper = new DatabaseHelper(this);

        RegisterBtn.setOnClickListener(v -> {
            String username = textUsername.getText().toString().trim();
            String email = textEmail.getText().toString().trim();
            String password = textPassword.getText().toString().trim();
            String phone = textPhone.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.insertUser(username, email, password, phone)) {
                Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });

        goTologinBtn.setOnClickListener(v -> alreadyHaveAccountOnClick());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back arrow click
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void alreadyHaveAccountOnClick() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}