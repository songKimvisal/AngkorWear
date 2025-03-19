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

public class HomeFragment extends Fragment {
    private RecyclerView carouselRecyclerView, productsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        carouselRecyclerView = view.findViewById(R.id.carousel_recycler_view);
        productsRecyclerView = view.findViewById(R.id.products_recycler_view);

        // Carousel setup (using same adapter for simplicity)
        carouselRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Product> carouselList = getSampleProducts();
        ProductAdapter carouselAdapter = new ProductAdapter(getContext(), carouselList);
        carouselRecyclerView.setAdapter(carouselAdapter);

        // Products setup
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Product> productList = getSampleProducts();
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
        productsRecyclerView.setAdapter(productAdapter);

        return view;
    }

    private List<Product> getSampleProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("1", "Shirt", 29.99, "https://placecats.com/millie/300/150", "Men", "Hello"));
        list.add(new Product("2", "Dress", 49.99, "https://placecats.com/millie/300/150", "Women", "Hello"));
        return list;
    }
}