package com.example.myapplication.admin;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.DatabaseHelper;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextItemName, editTextDescription, editTextPrice, editTextCategory;
    private Button buttonSave;
    private DatabaseHelper dbHelper;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCategory = findViewById(R.id.editTextCategory);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get the product to edit
        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            // Pre-fill the form with the product's details
            editTextItemName.setText(product.getName());
            editTextDescription.setText(product.getDescription());
            editTextPrice.setText(String.valueOf(product.getPrice()));
            editTextCategory.setText(product.getCategory());
        }

        buttonSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String name = editTextItemName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Please enter item name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please enter a category", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the product in the database
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);

        // Delete the old product and add the updated one (since we don't have an update method)
        dbHelper.deleteProductByName(product.getName());
        dbHelper.addProduct(product.getId(), name, price, null, category, description);

        Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}