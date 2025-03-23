package com.example.myapplication.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.FavoriteProductAdapter;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.FavoritesManager;

import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyView;
    private FavoriteProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.favorite_recycler_view);
        emptyView = view.findViewById(R.id.empty_favorites_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get favorites from FavoritesManager
        List<Product> favoriteList = FavoritesManager.getInstance().getFavoriteItems();
        adapter = new FavoriteProductAdapter(getContext(), favoriteList);
        recyclerView.setAdapter(adapter);

        // Show/hide empty view
        updateEmptyView(favoriteList);

        return view;
    }

    private void updateEmptyView(List<Product> favoriteList) {
        if (favoriteList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}