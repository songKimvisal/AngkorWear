package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.models.CartItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView orderSummaryRecyclerView;
    private TextView totalPriceTextView;
    private EditText shippingAddressEditText;
    private Spinner paymentMethodSpinner;
    private MaterialButton confirmOrderButton;

    private ArrayList<CartItem> cartItems;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize views
        orderSummaryRecyclerView = findViewById(R.id.checkout_order_summary);
        totalPriceTextView = findViewById(R.id.total_price);
        shippingAddressEditText = findViewById(R.id.shipping_address);
        paymentMethodSpinner = findViewById(R.id.payment_method);
        confirmOrderButton = findViewById(R.id.confirm_order_button);

        // Get cart data from intent
        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Set up RecyclerView for order summary
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter cartAdapter = new CartAdapter(cartItems);
        orderSummaryRecyclerView.setAdapter(cartAdapter);

        // Display total price
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));

        // Set up payment method spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);

        // Confirm order button click listener
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shippingAddress = shippingAddressEditText.getText().toString().trim();
                String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();

                if (shippingAddress.isEmpty()) {
                    shippingAddressEditText.setError("Please enter a shipping address");
                    return;
                }

                Toast.makeText(CheckoutActivity.this,
                        "Order confirmed! Shipping to: " + shippingAddress + "\nPayment Method: " + paymentMethod,
                        Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    // Handle the back button press
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}