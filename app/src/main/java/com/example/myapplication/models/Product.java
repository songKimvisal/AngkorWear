package com.example.myapplication.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private String category; // Women, Men, Kids
    private String description; // Product description
    private List<String> availableSizes; // List of available sizes (e.g., S, M, L, XL)
    private String selectedSize; // Currently selected size

    public Product(String id, String name, double price, String imageUrl, String category, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;
        this.availableSizes = new ArrayList<>(); // Initialize empty list
        this.selectedSize = null; // No size selected by default
    }

    // Constructor with sizes
    public Product(String id, String name, double price, String imageUrl, String category, String description, List<String> availableSizes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;
        this.availableSizes = availableSizes != null ? availableSizes : new ArrayList<>(); // Ensure not null
        this.selectedSize = availableSizes.isEmpty() ? null : availableSizes.get(0); // Default to first size
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getAvailableSizes() { return availableSizes; }
    public void setAvailableSizes(List<String> availableSizes) {
        this.availableSizes = availableSizes != null ? availableSizes : new ArrayList<>();
    }

    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String selectedSize) { this.selectedSize = selectedSize; }
}