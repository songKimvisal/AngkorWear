package com.example.myapplication.utils;

import com.example.myapplication.models.Product;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private static FavoritesManager instance;
    private List<Product> favoriteItems;

    private FavoritesManager() {
        favoriteItems = new ArrayList<>();
    }

    public static synchronized FavoritesManager getInstance() {
        if (instance == null) {
            instance = new FavoritesManager();
        }
        return instance;
    }

    public void addToFavorites(Product product) {
        if (!favoriteItems.contains(product)) {
            favoriteItems.add(product);
        }
    }

    public void removeFromFavorites(Product product) {
        favoriteItems.remove(product);
    }

    public List<Product> getFavoriteItems() {
        return new ArrayList<>(favoriteItems); // Return a copy to prevent external modification
    }

    public boolean isFavorite(Product product) {
        return favoriteItems.contains(product);
    }

    public void clearFavorites() {
        favoriteItems.clear();
    }
}