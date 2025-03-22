package com.example.myapplication.admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.CategoryAdapter;
import com.example.myapplication.models.CategoryViewModel;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class AddRemoveCategoryActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter adapter;
    private TextInputEditText nameInput;
    private List<String> categories;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_add_remove_category);
        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.category_recycler_view);
        nameInput = findViewById(R.id.category_name_input);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categories = categoryViewModel.getCategoryList().getValue() != null ?
                categoryViewModel.getCategoryList().getValue() : new ArrayList<>();
        adapter = new CategoryAdapter(this, categories, this::removeCategory);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.add_category_btn).setOnClickListener(v -> addCategory());

        // Observe changes to the category list
        categoryViewModel.getCategoryList().observe(this, categoryList -> {
            categories = categoryList;
            adapter = new CategoryAdapter(this, categoryList, this::removeCategory);
            recyclerView.setAdapter(adapter);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addCategory() {
        String name = nameInput.getText().toString().trim();
        if (!name.isEmpty()) {
            categoryViewModel.addCategory(name);
            nameInput.setText("");
        }
    }

    private void removeCategory(String category) {
        categoryViewModel.removeCategory(category);
    }
}