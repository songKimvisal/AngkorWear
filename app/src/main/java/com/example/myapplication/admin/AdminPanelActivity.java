package com.example.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class AdminPanelActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardAddRemoveCategory;
    private CardView cardAddRemoveItems;
    private CardView cardEditItem;
    private CardView cardUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_panel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize CardViews
        cardAddRemoveCategory = findViewById(R.id.cardAddRemoveCategory);
        cardAddRemoveItems = findViewById(R.id.cardAddRemoveItems);
        cardEditItem = findViewById(R.id.cardEditItem);
        cardUserList = findViewById(R.id.cardUserList);

        // Set OnClickListeners
        cardAddRemoveCategory.setOnClickListener(this);
        cardAddRemoveItems.setOnClickListener(this);
        cardEditItem.setOnClickListener(this);
        cardUserList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.cardAddRemoveCategory) {
            // First Block: Add Product
            intent = new Intent(this, AddRemoveItemActivity.class);
            intent.putExtra("mode", "add"); // Indicate that the activity should focus on adding
        } else if (v.getId() == R.id.cardAddRemoveItems) {
            // Second Block: Edit Product
            intent = new Intent(this, EditItemActivity.class);
        } else if (v.getId() == R.id.cardEditItem) {
            // Third Block: Delete Product
            intent = new Intent(this, AddRemoveItemActivity.class);
            intent.putExtra("mode", "delete"); // Indicate that the activity should focus on deleting
        } else if (v.getId() == R.id.cardUserList) {
            // Fourth Block: User List
            intent = new Intent(this, UserListActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}