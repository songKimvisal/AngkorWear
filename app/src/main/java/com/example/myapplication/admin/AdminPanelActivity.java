package com.example.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

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
    private CardView cardRemoveItems; // Added for the "Remove Items" card
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_panel);
        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize CardViews
        cardAddRemoveCategory = findViewById(R.id.cardAddRemoveCategory);
        cardAddRemoveItems = findViewById(R.id.cardAdd_Items); // Updated ID to match XML
        cardEditItem = findViewById(R.id.cardEditItem);
        cardUserList = findViewById(R.id.cardUserList);
        cardRemoveItems = findViewById(R.id.Remove_tems); // Initialize the "Remove Items" card

        // Set OnClickListeners
        cardAddRemoveCategory.setOnClickListener(this);
        cardAddRemoveItems.setOnClickListener(this);
        cardEditItem.setOnClickListener(this);
        cardUserList.setOnClickListener(this);
        cardRemoveItems.setOnClickListener(this); // Set listener for the "Remove Items" card
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.cardAddRemoveCategory) {
            intent = new Intent(this, AddRemoveCategoryActivity.class); // Replace with your actual activity
        } else if (v.getId() == R.id.cardAdd_Items) { // Updated ID to match XML
            intent = new Intent(this, AddItemActivity.class); // Replace with your actual activity
        } else if (v.getId() == R.id.cardEditItem) {
            intent = new Intent(this, EditItemActivity.class); // Replace with your actual activity
        } else if (v.getId() == R.id.cardUserList) {
            intent = new Intent(this, UserListActivity.class); // Replace with your actual activity
        } else if (v.getId() == R.id.Remove_tems) { // Handle click for "Remove Items"
            intent = new Intent(this, RemoveItemsActivity.class); // Replace with your actual activity
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back arrow click
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}