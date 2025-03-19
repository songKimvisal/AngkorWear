package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.cartItemName.setText(cartItem.getName());
        holder.cartItemQuantity.setText("Qty: " + cartItem.getQuantity());
        holder.cartItemPrice.setText(String.format("$%.2f", cartItem.getPrice() * cartItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView cartItemName, cartItemQuantity, cartItemPrice;

        CartViewHolder(View itemView) {
            super(itemView);
            cartItemName = itemView.findViewById(R.id.cart_item_name);
            cartItemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            cartItemPrice = itemView.findViewById(R.id.cart_item_price);
        }
    }
}