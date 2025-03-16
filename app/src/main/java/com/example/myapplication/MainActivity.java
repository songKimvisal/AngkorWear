package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;  // Add this import
import androidx.fragment.app.Fragment;

import com.example.myapplication.fragments.CartFragment;
import com.example.myapplication.fragments.FavoriteFragment;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.example.myapplication.fragments.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");  // Set your desired title
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                toolbar.setTitle("Home");  // Update toolbar title
            } else if (itemId == R.id.nav_shop) {
                selectedFragment = new ShopFragment();
                toolbar.setTitle("Shop");
            } else if (itemId == R.id.nav_cart) {
                selectedFragment = new CartFragment();
                toolbar.setTitle("Cart");
            } else if (itemId == R.id.nav_favorite) {
                selectedFragment = new FavoriteFragment();
                toolbar.setTitle("Favorites");
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                toolbar.setTitle("Profile");
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Load HomeFragment by default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}