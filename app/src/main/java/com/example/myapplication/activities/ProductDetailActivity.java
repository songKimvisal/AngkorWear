package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Product;
import com.google.android.material.button.MaterialButton;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

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

        addToCartButton.setOnClickListener(v -> Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show());
        favoriteButton.setOnClickListener(v -> Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show());
        checkoutButton.setOnClickListener(v -> Toast.makeText(this, "Proceeding to Checkout", Toast.LENGTH_SHORT).show());
    }
}
