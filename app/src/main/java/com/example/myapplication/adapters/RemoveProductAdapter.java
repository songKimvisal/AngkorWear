package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Product;

import java.util.List;

public class RemoveProductAdapter extends RecyclerView.Adapter<RemoveProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductRemoveListener removeListener;

    public interface OnProductRemoveListener {
        void onProductRemove(Product product);
    }

    public RemoveProductAdapter(Context context, List<Product> productList, OnProductRemoveListener listener) {
        this.context = context;
        this.productList = productList;
        this.removeListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_remove_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductPrice.setText("Price: $" + String.format("%.2f", product.getPrice()));
        holder.textViewProductCategory.setText("Category: " + product.getCategory());

        holder.buttonRemove.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onProductRemove(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName, textViewProductPrice, textViewProductCategory;
        Button buttonRemove;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductCategory = itemView.findViewById(R.id.textViewProductCategory);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}