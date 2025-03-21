package com.example.myapplication.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.UUID;

public class AddRemoveItemActivity extends AppCompatActivity {

    private EditText editTextItemName, editTextDescription, editTextPrice, editTextCategory;
    private Button buttonAddItem;
    private RecyclerView recyclerViewProducts;
    private TextView textViewTitle, textViewRemoveTitle;
    private LinearLayout addItemSection;
    private AdminProductAdapter adminProductAdapter;
    private DatabaseHelper dbHelper;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_remove_item);
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
        buttonAddItem = findViewById(R.id.buttonAddItem);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewRemoveTitle = findViewById(R.id.textViewRemoveTitle);
        addItemSection = findViewById(R.id.addItemSection); // We'll add this ID to the layout

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Fetch products from the database
        productList = dbHelper.getAllProducts();

        // Set up RecyclerView for product list
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        adminProductAdapter = new AdminProductAdapter(this, productList, product -> {
            // Handle delete action
            int rowsAffected = dbHelper.deleteProductByName(product.getName());
            if (rowsAffected > 0) {
                Toast.makeText(this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                // Refresh the product list
                productList = dbHelper.getAllProducts();
                adminProductAdapter.updateList(productList);
            } else {
                Toast.makeText(this, "Failed to remove item", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewProducts.setAdapter(adminProductAdapter);

        // Check the mode (add or delete)
        String mode = getIntent().getStringExtra("mode");
        if ("delete".equals(mode)) {
            // Hide the add product form
            addItemSection.setVisibility(View.GONE);
            textViewTitle.setVisibility(View.GONE);
            textViewRemoveTitle.setText("Delete Product");
        } else {
            // Default to add mode
            buttonAddItem.setOnClickListener(v -> addItemToDatabase());
        }
    }

    private void addItemToDatabase() {
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

        String productId = UUID.randomUUID().toString();
        byte[] imageBytes = null; // Since we're ignoring images for now

        // Pass the description parameter to addProduct
        dbHelper.addProduct(productId, name, price, imageBytes, category, description);
        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
        clearInputFields();

        // Refresh the product list
        productList = dbHelper.getAllProducts();
        adminProductAdapter.updateList(productList);
    }

    private void clearInputFields() {
        editTextItemName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextCategory.setText("");
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}