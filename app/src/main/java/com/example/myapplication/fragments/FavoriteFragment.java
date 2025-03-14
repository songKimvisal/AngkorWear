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

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.favorite_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Product> favoriteList = getSampleFavorites();
        ProductAdapter adapter = new ProductAdapter(getContext(), favoriteList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Product> getSampleFavorites() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("2", "Dress", 49.99, "https://example.com/dress.jpg", "Women"));
        return list;
    }
}