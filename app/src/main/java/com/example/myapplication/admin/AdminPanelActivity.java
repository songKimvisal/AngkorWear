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
            intent = new Intent(this, AddRemoveCategoryActivity.class); // Replace with your actual activity
        } else if (v.getId() == R.id.cardAddRemoveItems) {
            intent = new Intent(this, AddRemoveItemActivity.class); // Replace with your actual activity
        } else if (v.getId() == R.id.cardEditItem) {
            intent = new Intent(this, EditItemActivity.class); // Replace with your actual activity
        } else if (v.getId() == R.id.cardUserList) {
            intent = new Intent(this, UserListActivity.class); // Replace with your actual activity
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}