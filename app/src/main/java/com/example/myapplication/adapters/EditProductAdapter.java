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

public class EditProductAdapter extends RecyclerView.Adapter<EditProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductEditListener editListener;

    public interface OnProductEditListener {
        void onProductEdit(Product product);
    }

    public EditProductAdapter(Context context, List<Product> productList, OnProductEditListener listener) {
        this.context = context;
        this.productList = productList;
        this.editListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_edit_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductPrice.setText("Price: $" + String.format("%.2f", product.getPrice()));
        holder.textViewProductCategory.setText("Category: " + product.getCategory());

        holder.buttonEdit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onProductEdit(product);
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
        Button buttonEdit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductCategory = itemView.findViewById(R.id.textViewProductCategory);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}