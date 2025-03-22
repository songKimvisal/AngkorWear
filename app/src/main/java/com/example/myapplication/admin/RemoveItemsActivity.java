package com.example.myapplication.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewRemoveItems = findViewById(R.id.recyclerViewRemoveItems);

        if (recyclerViewRemoveItems == null) {
            Log.e(TAG, "RecyclerView not found in layout");
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
        adapter = new ProductListAdapter(this, productList, this, "Remove");
        recyclerViewRemoveItems.setAdapter(adapter);

        loadProducts();
    }

    private void loadProducts() {
        try {
            productList.clear();
            List<Product> products = dbHelper.getAllProducts();
            productList.addAll(products);
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

    @Override
    public void onProductClick(Product product) {
        try {
            dbHelper.deleteProduct(product.getId());
            productList.remove(product);
            adapter.notifyDataSetChanged();
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
}