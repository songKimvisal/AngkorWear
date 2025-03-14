package com.example.myapplication.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private String category; // Women, Men, Kids

    public Product(String id, String name, double price, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
}
