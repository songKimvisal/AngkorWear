package com.example.myapplication.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.CategoryViewModel;
import com.example.myapplication.utils.DatabaseHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopFragment extends Fragment {
    private static final String TAG = "ShopFragment";
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList, filteredList;
    private ChipGroup chipGroup;
    private CategoryViewModel categoryViewModel;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = view.findViewById(R.id.shop_recycler_view);
        chipGroup = view.findViewById(R.id.chip_group);
        TextInputEditText searchInput = view.findViewById(R.id.search_input);

        if (recyclerView == null || chipGroup == null || searchInput == null) {
            Log.e(TAG, "One or more views not found in layout");
            return view;
        }

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        } else {
            Log.e(TAG, "Context is null");
            return view;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        loadProductsFromDatabase();

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), this::setupCategoryChips);

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

        return view;
    }

    private void loadProductsFromDatabase() {
        try {
            productList.clear();
            List<Product> products = dbHelper.getAllProducts();
            productList.addAll(products);
            filteredList.clear();
            filteredList.addAll(productList);
            adapter.notifyDataSetChanged();
            Log.d(TAG, "Loaded " + products.size() + " products");
        } catch (Exception e) {
            Log.e(TAG, "Error loading products: " + e.getMessage());
        }
    }

    private void setupCategoryChips(List<String> categoryList) {
        if (categoryList == null) return;
        chipGroup.removeAllViews();
        for (String category : categoryList) {
            Chip newChip = new Chip(getContext());
            newChip.setId(View.generateViewId());
            newChip.setText(category);
            newChip.setCheckable(true);
            newChip.setOnClickListener(v -> filterByCategory(category));
            chipGroup.addView(newChip);
        }
        Chip sortChip = new Chip(getContext());
        sortChip.setId(View.generateViewId());
        sortChip.setText("Sort by Price");
        sortChip.setCheckable(true);
        sortChip.setOnClickListener(v -> {
            Collections.sort(filteredList, (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
            adapter.notifyDataSetChanged();
        });
        chipGroup.addView(sortChip);
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
    public void onResume() {
        super.onResume();
        loadProductsFromDatabase(); // Refresh product list when fragment resumes
    }

    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}