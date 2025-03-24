package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.CartItem;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.CartManager;
import com.example.myapplication.utils.FavoritesManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;
    private int quantity = 1;
    private FavoritesManager favoritesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }

        // Get product from intent
        product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize FavoritesManager
        favoritesManager = FavoritesManager.getInstance();

        // UI elements
        ImageView productImage = findViewById(R.id.product_image);
        TextView productName = findViewById(R.id.product_name);
        TextView productPrice = findViewById(R.id.product_price);
        TextView productDescription = findViewById(R.id.product_description);
        Button decrementButton = findViewById(R.id.decrement_quantity_button);
        TextView quantityText = findViewById(R.id.quantity_text);
        Button incrementButton = findViewById(R.id.increment_quantity_button);
        MaterialButton addToCartButton = findViewById(R.id.add_to_cart_button);
        MaterialButton favoriteButton = findViewById(R.id.favorite_button);
        MaterialButton checkoutButton = findViewById(R.id.checkout_button);

        // Set product details
        productName.setText(product.getName());
        productPrice.setText("$" + String.format("%.2f", product.getPrice()));
        productDescription.setText(product.getDescription());
        Glide.with(this).load(product.getImageUrl()).into(productImage);

        // Set initial quantity
        quantityText.setText(String.valueOf(quantity));

        // Quantity selector buttons
        decrementButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        incrementButton.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });

        // Add to Cart button
        addToCartButton.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(product.getName(), product.getPrice(), quantity);
            CartManager.getInstance().addToCart(cartItem);
            Toast.makeText(this, "Added " + quantity + " " + product.getName() + "(s) to Cart", Toast.LENGTH_SHORT).show();
        });

        // Favorite button (only adds, no toggle)
        favoriteButton.setText("Add to Favorites"); // Ensure button text is static
        favoriteButton.setOnClickListener(v -> {
            if (!favoritesManager.isFavorite(product)) {
                favoritesManager.addToFavorites(product);
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already in Favorites", Toast.LENGTH_SHORT).show();
            }
        });

        // Checkout button
        checkoutButton.setOnClickListener(v -> {
            CartManager cartManager = CartManager.getInstance();
            if (cartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartManager.getCartItems()));
            intent.putExtra("totalPrice", cartManager.getTotalPrice());
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}