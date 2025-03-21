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
import com.example.myapplication.utils.DatabaseHelper;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView carouselRecyclerView, productsRecyclerView;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        carouselRecyclerView = view.findViewById(R.id.carousel_recycler_view);
        productsRecyclerView = view.findViewById(R.id.products_recycler_view);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());

        // Fetch products from the database
        List<Product> productList = dbHelper.getAllProducts();

        // Carousel setup
        carouselRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ProductAdapter carouselAdapter = new ProductAdapter(getContext(), productList);
        carouselRecyclerView.setAdapter(carouselAdapter);

        // Products setup
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
        productsRecyclerView.setAdapter(productAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}