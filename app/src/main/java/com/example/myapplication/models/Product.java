package com.example.myapplication.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private double price;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String imageUrl;
    private String category; // Women, Men, Kids
    private String description; // Add description field

    public Product(String id, String name, double price, String imageUrl, String category, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
}