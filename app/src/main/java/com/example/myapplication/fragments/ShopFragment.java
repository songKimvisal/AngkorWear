package com.example.myapplication.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.DatabaseHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList, filteredList;
    private Chip chipWomen, chipMen, chipKids, chipSortPrice;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = view.findViewById(R.id.shop_recycler_view);
        chipWomen = view.findViewById(R.id.chip_women);
        chipMen = view.findViewById(R.id.chip_men);
        chipKids = view.findViewById(R.id.chip_kids);
        chipSortPrice = view.findViewById(R.id.chip_sort_price);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());

        // Fetch products from the database
        productList = dbHelper.getAllProducts();
        filteredList = new ArrayList<>(productList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        // Search functionality
        TextInputEditText searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString());
            }
        });

        // Category filters
        chipWomen.setOnClickListener(v -> filterByCategory("Women"));
        chipMen.setOnClickListener(v -> filterByCategory("Men"));
        chipKids.setOnClickListener(v -> filterByCategory("Kids"));

        // Sort by price
        chipSortPrice.setOnClickListener(v -> {
            Collections.sort(filteredList, (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    private void filterProducts(String query) {
        filteredList.clear();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void filterByCategory(String category) {
        filteredList.clear();
        for (Product product : productList) {
            if (product.getCategory().equals(category)) {
                filteredList.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}