package com.example.myapplication.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EditProductAdapter;
import com.example.myapplication.models.CategoryViewModel;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EditItemActivity extends AppCompatActivity implements EditProductAdapter.OnProductEditListener {
    private static final String TAG = "EditItemActivity";
    private RecyclerView recyclerViewProducts;
    private EditProductAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;
    private CategoryViewModel categoryViewModel;

    // Edit form views
    private LinearLayout editForm;
    private EditText editTextItemName, editTextDescription, editTextPrice, editTextImageUrl;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        editForm = findViewById(R.id.editForm);
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Check for null views
        if (recyclerViewProducts == null || editForm == null || editTextItemName == null ||
                editTextPrice == null || editTextImageUrl == null || spinnerCategory == null ||
                buttonSave == null || buttonCancel == null) {
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

        // Initialize RecyclerView
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new EditProductAdapter(this, productList, this);
        recyclerViewProducts.setAdapter(adapter);

        // Load products
        loadProducts();

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

        // Set up button listeners
        buttonSave.setOnClickListener(v -> saveChanges());
        buttonCancel.setOnClickListener(v -> hideEditForm());
    }

    private void loadProducts() {
        try {
            productList.clear();
            List<Product> products = dbHelper.getAllProducts();
            productList.addAll(products);
            adapter.notifyDataSetChanged();
            Log.d(TAG, "Loaded " + products.size() + " products");
            if (products.isEmpty()) {
                Toast.makeText(this, "No products available to edit", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading products: " + e.getMessage());
            Toast.makeText(this, "Failed to load products: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProductEdit(Product product) {
        selectedProduct = product;
        showEditForm();
    }

    private void showEditForm() {
        if (selectedProduct == null) return;

        // Populate the form with the selected product's details
        editTextItemName.setText(selectedProduct.getName());
        editTextDescription.setText(selectedProduct.getDescription());
        editTextPrice.setText(String.valueOf(selectedProduct.getPrice()));
        editTextImageUrl.setText(selectedProduct.getImageUrl());

        // Set the spinner to the product's current category
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(selectedProduct.getCategory());
            if (position >= 0) {
                spinnerCategory.setSelection(position);
            }
        }

        // Show the edit form and hide the product list
        editForm.setVisibility(View.VISIBLE);
        recyclerViewProducts.setVisibility(View.GONE);
    }

    private void hideEditForm() {
        // Clear the form and hide it
        editTextItemName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextImageUrl.setText("");
        spinnerCategory.setSelection(0);
        selectedProduct = null;

        editForm.setVisibility(View.GONE);
        recyclerViewProducts.setVisibility(View.VISIBLE);
    }

    private void saveChanges() {
        if (selectedProduct == null) {
            Toast.makeText(this, "No product selected", Toast.LENGTH_SHORT).show();
            return;
        }

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
            description = description.replace("'", "''");
        } else {
            description = null;
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

        try {
            Log.d(TAG, "Updating product - ID: " + selectedProduct.getId() + ", Name: " + name + ", Price: " + price +
                    ", Image: " + (imageUrl == null ? "null" : imageUrl) + ", Category: " + category + ", Description: " + (description == null ? "null" : description));
            dbHelper.updateProduct(selectedProduct.getId(), name, price, imageUrl, category, description);

            // Update the local product list
            selectedProduct = new Product(selectedProduct.getId(), name, price, imageUrl, category, description);
            int index = productList.indexOf(selectedProduct);
            if (index >= 0) {
                productList.set(index, selectedProduct);
                adapter.notifyDataSetChanged();
            }

            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            hideEditForm();
        } catch (Exception e) {
            Log.e(TAG, "Error updating product: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to update product: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}