package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;

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

        product = (Product) getIntent().getSerializableExtra("product");

        ImageView productImage = findViewById(R.id.product_image);
        TextView productName = findViewById(R.id.product_name);
        TextView productPrice = findViewById(R.id.product_price);
        MaterialButton addToCartButton = findViewById(R.id.add_to_cart_button);
        MaterialButton favoriteButton = findViewById(R.id.favorite_button);
        MaterialButton checkoutButton = findViewById(R.id.checkout_button);

        productName.setText(product.getName());
        productPrice.setText("$" + product.getPrice());
        Glide.with(this).load(product.getImageUrl()).into(productImage);

        // Add to Cart button
        addToCartButton.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(product.getName(), product.getPrice(), 1);
            CartManager.getInstance().addToCart(cartItem);
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
        });

        // Favorite button
        favoriteButton.setOnClickListener(v -> Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show());

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
}