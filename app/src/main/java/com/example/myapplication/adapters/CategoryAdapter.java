package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<String> categoryList;
    private Context context;
    private OnCategoryRemoveListener listener;

    public interface OnCategoryRemoveListener {
        void onRemove(String category);
    }

    public CategoryAdapter(Context context, List<String> categoryList, OnCategoryRemoveListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.nameTextView.setText(category);
        holder.removeButton.setOnClickListener(v -> listener.onRemove(category));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.category_name);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}