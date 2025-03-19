package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.CheckoutActivity;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.models.CartItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private MaterialButton checkoutButton;
    private ArrayList<CartItem> cartItems;
    private double totalPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_cart.xml layout
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize views
        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        checkoutButton = view.findViewById(R.id.checkout_button);

        if (cartRecyclerView == null || checkoutButton == null) {
            throw new IllegalStateException("Required views not found in fragment_cart.xml");
        }

        // Sample cart data (replace with your actual data source)
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Shirt", 29.99, 1)); // Example item
        totalPrice = 29.99; // Calculate total dynamically in a real app

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CartAdapter cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

        // Checkout button click listener
        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            intent.putParcelableArrayListExtra("cartItems", cartItems);
            intent.putExtra("totalPrice", totalPrice);
            startActivity(intent);
        });

        return view;
    }
}