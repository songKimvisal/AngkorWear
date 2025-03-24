package com.example.myapplication.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ProductListAdapter;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class RemoveItemsActivity extends AppCompatActivity implements ProductListAdapter.OnProductClickListener {
    private static final String TAG = "RemoveItemsActivity";
    private RecyclerView recyclerViewRemoveItems;
    private ProductListAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredProductList; // For search functionality
    private DatabaseHelper dbHelper;
    private Toolbar toolbar;
    private EditText searchEditText; // Added search field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_items);

        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerViewRemoveItems = findViewById(R.id.recyclerViewRemoveItems);
        searchEditText = findViewById(R.id.searchEditText); // Initialize search field

        if (recyclerViewRemoveItems == null || searchEditText == null) {
            Log.e(TAG, "RecyclerView or Search EditText not found in layout");
            Toast.makeText(this, "UI initialization error", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            dbHelper = new DatabaseHelper(this);
            Log.d(TAG, "DatabaseHelper initialized");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize DatabaseHelper: " + e.getMessage());
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        recyclerViewRemoveItems.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>(); // Initialize filtered list
        adapter = new ProductListAdapter(this, filteredProductList, this, "Remove");
        recyclerViewRemoveItems.setAdapter(adapter);

        loadProducts();
        setupSearch();
    }

    private void loadProducts() {
        try {
            productList.clear();
            filteredProductList.clear();
            List<Product> products = dbHelper.getAllProducts();
            productList.addAll(products);
            filteredProductList.addAll(products);
            adapter.notifyDataSetChanged();
            Log.d(TAG, "Loaded " + products.size() + " products");
            if (products.isEmpty()) {
                Toast.makeText(this, "No products available to remove", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading products: " + e.getMessage());
            Toast.makeText(this, "Failed to load products: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString());
            }
        });
    }

    private void filterProducts(String query) {
        filteredProductList.clear();
        if (TextUtils.isEmpty(query)) {
            filteredProductList.addAll(productList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredProductList.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
        Log.d(TAG, "Filtered products: " + filteredProductList.size());
    }

    @Override
    public void onProductClick(Product product) {
        try {
            dbHelper.deleteProduct(product.getId());
            productList.remove(product);
            filterProducts(searchEditText.getText().toString()); // Update filtered list after removal
            Toast.makeText(this, "Product removed successfully", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Product removed: " + product.getName());
        } catch (Exception e) {
            Log.e(TAG, "Error removing product: " + e.getMessage());
            Toast.makeText(this, "Failed to remove product: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}