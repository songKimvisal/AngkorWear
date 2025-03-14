package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Product> cartList = getSampleCart();
        ProductAdapter adapter = new ProductAdapter(getContext(), cartList);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.checkout_button).setOnClickListener(v -> {
            // Handle checkout
        });

        return view;
    }

    private List<Product> getSampleCart() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("1", "Shirt", 29.99, "https://example.com/shirt.jpg", "Men"));
        return list;
    }
}