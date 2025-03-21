package com.example.myapplication.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.myapplication.models.CategoryViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList, filteredList;
    private ChipGroup chipGroup;
    private CategoryViewModel categoryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = view.findViewById(R.id.shop_recycler_view);
        chipGroup = view.findViewById(R.id.chip_group);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productList = getSampleProducts();
        filteredList = new ArrayList<>(productList);
        adapter = new ProductAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        // Observe category list changes
        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), this::setupCategoryChips);

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

        return view;
    }

    private void setupCategoryChips(List<String> categoryList) {
        chipGroup.removeAllViews();

        // Add category chips
        for (String category : categoryList) {
            Chip newChip = new Chip(getContext());
            newChip.setId(View.generateViewId());
            newChip.setText(category);
            newChip.setCheckable(true);
            newChip.setOnClickListener(v -> filterByCategory(category));
            chipGroup.addView(newChip);
        }

        // Add sort chip separately
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

    private List<Product> getSampleProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("1", "Shirt", 29.99, "https://placecats.com/300/200", "Men", "A comfortable cotton shirt"));
        list.add(new Product("2", "Dress", 49.99, "https://placecats.com/300/200", "Women", "An elegant dress"));
        list.add(new Product("3", "Jacket", 19.99, "https://placecats.com/300/200", "Kids", "A warm jacket"));
        return list;
    }
}