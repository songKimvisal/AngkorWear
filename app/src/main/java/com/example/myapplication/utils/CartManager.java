package com.example.myapplication.utils;

import com.example.myapplication.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private double totalPrice;

    private CartManager() {
        cartItems = new ArrayList<>();
        totalPrice = 0.0;
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(CartItem item) {
        // Check if the product already exists in the cart
        for (CartItem cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                totalPrice += item.getPrice() * item.getQuantity();
                return;
            }
        }
        // If the product doesn't exist, add it as a new item
        cartItems.add(item);
        totalPrice += item.getPrice() * item.getQuantity();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void clearCart() {
        cartItems.clear();
        totalPrice = 0.0;
    }
}