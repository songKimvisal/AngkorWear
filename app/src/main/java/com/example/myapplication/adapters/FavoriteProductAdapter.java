package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Product;
import com.example.myapplication.utils.FavoritesManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> favoriteList;

    public FavoriteProductAdapter(Context context, List<Product> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = favoriteList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + String.format("%.2f", product.getPrice()));
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        holder.unfavoriteButton.setOnClickListener(v -> {
            FavoritesManager.getInstance().removeFromFavorites(product);
            favoriteList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteList.size());
            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();

            // Update empty state
            if (favoriteList.isEmpty()) {
                View rootView = holder.itemView.getRootView();
                rootView.findViewById(R.id.favorite_recycler_view).setVisibility(View.GONE);
                rootView.findViewById(R.id.empty_favorites_text).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        MaterialButton unfavoriteButton;

        ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.favorite_product_image);
            productName = itemView.findViewById(R.id.favorite_product_name);
            productPrice = itemView.findViewById(R.id.favorite_product_price);
            unfavoriteButton = itemView.findViewById(R.id.unfavorite_button);
        }
    }
}