package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.admin.EditItemActivity;
import com.example.myapplication.models.Product;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener;
    private boolean isEditMode;

    public interface OnDeleteClickListener {
        void onDeleteClick(Product product);
    }

    public AdminProductAdapter(Context context, List<Product> productList, OnDeleteClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.onDeleteClickListener = listener;
        this.isEditMode = context instanceof EditItemActivity;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());
        holder.productDescription.setText(product.getDescription());

        // Change button text based on mode
        if (isEditMode) {
            holder.buttonDelete.setText("Edit");
        } else {
            holder.buttonDelete.setText("Delete");
        }

        holder.buttonDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDescription;
        MaterialButton buttonDelete;

        ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDescription = itemView.findViewById(R.id.product_description);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    // Method to update the list after deletion
    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }
}