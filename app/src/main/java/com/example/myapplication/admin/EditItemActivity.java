package com.example.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AdminProductAdapter;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.DatabaseHelper;

import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private AdminProductAdapter adminProductAdapter;
    private DatabaseHelper dbHelper;
    private List<Product> productList;

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

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Fetch products from the database
        productList = dbHelper.getAllProducts();

        // Set up RecyclerView for product list
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        adminProductAdapter = new AdminProductAdapter(this, productList, product -> {
            // On click, navigate to a new activity to edit the product
            Intent intent = new Intent(EditItemActivity.this, EditProductActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
        recyclerViewProducts.setAdapter(adminProductAdapter);
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}