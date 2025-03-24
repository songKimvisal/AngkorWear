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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductClickListener clickListener;
    private String buttonText;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductListAdapter(Context context, List<Product> productList, OnProductClickListener listener, String buttonText) {
        this.context = context;
        this.productList = productList;
        this.clickListener = listener;
        this.buttonText = buttonText;
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

        holder.buttonAction.setText(buttonText);

        holder.buttonAction.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onProductClick(product);
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
        Button buttonAction;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductCategory = itemView.findViewById(R.id.textViewProductCategory);
            buttonAction = itemView.findViewById(R.id.buttonRemove); // Note: Rename this ID in the layout
        }
    }
}