package com.example.myapplication.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.models.CategoryViewModel;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.DatabaseHelper;

import java.util.List;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = "AddItemActivity";
    private EditText editTextItemName, editTextDescription, editTextPrice, editTextImageUrl;
    private Button buttonAddItem;
    private Spinner spinnerCategory;
    private ImageView imageViewItem;
    private DatabaseHelper dbHelper;
    private CategoryViewModel categoryViewModel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add item ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        // Initialize UI elements
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imageViewItem = findViewById(R.id.imageViewItem);

        // Check for null views
        if (editTextItemName == null || editTextPrice == null || editTextImageUrl == null ||
                buttonAddItem == null || spinnerCategory == null) {
            Log.e(TAG, "One or more UI elements not found");
            Toast.makeText(this, "UI initialization error", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize DatabaseHelper
        try {
            dbHelper = new DatabaseHelper(this);
            Log.d(TAG, "DatabaseHelper initialized");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize DatabaseHelper: " + e.getMessage());
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        // Initialize CategoryViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Setup category spinner
        categoryViewModel.getCategoryList().observe(this, categories -> {
            if (categories == null || categories.isEmpty()) {
                Log.w(TAG, "Category list is null or empty");
                Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);
            Log.d(TAG, "Spinner populated with " + categories.size() + " categories");
        });

        // Set up listener
        buttonAddItem.setOnClickListener(v -> addItemToDatabase());
    }

    private void addItemToDatabase() {
        String name = editTextItemName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String imageUrl = editTextImageUrl.getText().toString().trim();
        String category;

        // Check if spinner has a selected item
        if (spinnerCategory.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "No category selected in spinner");
            return;
        }
        category = spinnerCategory.getSelectedItem().toString();

        // Validate inputs (imageUrl is optional)
        if (TextUtils.isEmpty(name)) {
            editTextItemName.setError("Name is required");
            Log.w(TAG, "Name is empty");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            editTextPrice.setError("Price is required");
            Log.w(TAG, "Price is empty");
            return;
        }
        if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Category cannot be empty", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Category is empty");
            return;
        }

        // Allow imageUrl to be null or empty
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = null;
            Log.d(TAG, "No image URL provided, setting to null");
        }

        // Sanitize description to prevent SQL issues
        if (!TextUtils.isEmpty(description)) {
            description = description.replace("'", "''"); // Escape single quotes
        } else {
            description = null; // Set to null if empty
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                editTextPrice.setError("Price cannot be negative");
                Log.w(TAG, "Price is negative: " + price);
                return;
            }
        } catch (NumberFormatException e) {
            editTextPrice.setError("Invalid price format");
            Log.e(TAG, "Invalid price format: " + priceStr);
            return;
        }

        String productId = UUID.randomUUID().toString();
        try {
            Log.d(TAG, "Attempting to add product - ID: " + productId + ", Name: " + name + ", Price: " + price +
                    ", Image: " + (imageUrl == null ? "null" : imageUrl) + ", Category: " + category + ", Description: " + (description == null ? "null" : description));
            dbHelper.addProduct(productId, name, price, imageUrl, category, description);
            Log.d(TAG, "Product added successfully - ID: " + productId);

            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
            clearInputFields();
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error adding product to database: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearInputFields() {
        editTextItemName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextImageUrl.setText("");
        imageViewItem.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
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

}