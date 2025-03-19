package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.CheckoutActivity;
import com.example.myapplication.adapters.CartAdapter;
import com.example.myapplication.models.CartItem;
import com.example.myapplication.utils.CartManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private MaterialButton checkoutButton;
    private ArrayList<CartItem> cartItems;
    private double totalPrice;
    private CartAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        checkoutButton = view.findViewById(R.id.checkout_button);

        if (cartRecyclerView == null || checkoutButton == null) {
            throw new IllegalStateException("Required views not found in fragment_cart.xml");
        }

        // Get cart data from CartManager
        CartManager cartManager = CartManager.getInstance();
        cartItems = new ArrayList<>(cartManager.getCartItems());
        totalPrice = cartManager.getTotalPrice();

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

        // Checkout button click listener
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            intent.putParcelableArrayListExtra("cartItems", cartItems);
            intent.putExtra("totalPrice", totalPrice);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh cart data when returning to CartFragment
        CartManager cartManager = CartManager.getInstance();
        cartItems.clear();
        cartItems.addAll(cartManager.getCartItems());
        totalPrice = cartManager.getTotalPrice();
        cartAdapter.notifyDataSetChanged();
    }
}