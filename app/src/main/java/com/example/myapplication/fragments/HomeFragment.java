package com.example.myapplication.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.CarouselAdapter;
import com.example.myapplication.adapters.ProductAdapter;
import com.example.myapplication.models.Product;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private ViewPager2 carouselViewPager;
    private TabLayout carouselIndicators;
    private RecyclerView productsRecyclerView;
    private CarouselAdapter carouselAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable carouselRunnable;
    private static final long AUTO_SCROLL_DELAY = 3000; // 3 seconds

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        carouselViewPager = view.findViewById(R.id.carousel_view_pager);
        carouselIndicators = view.findViewById(R.id.carousel_indicators);
        productsRecyclerView = view.findViewById(R.id.products_recycler_view);

        // Setup carousel
        setupCarousel();

        // Setup products RecyclerView
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Product> productList = getSampleProducts();
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
        productsRecyclerView.setAdapter(productAdapter);

        return view;
    }

    private void setupCarousel() {
        List<String> carouselImages = Arrays.asList(
                "https://placecats.com/millie/300/150",
                "https://placecats.com/leo/300/150",
                "https://placecats.com/tigger/300/150",
                "https://placecats.com/misty/300/150"
        );

        carouselAdapter = new CarouselAdapter(getContext(), carouselImages);
        carouselViewPager.setAdapter(carouselAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(carouselIndicators, carouselViewPager,
                (tab, position) -> {
                    // No title needed for dots
                }).attach();

        // Auto-scroll implementation
        carouselRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = carouselViewPager.getCurrentItem() + 1;
                if (nextItem >= carouselAdapter.getItemCount()) {
                    nextItem = 0;
                }
                carouselViewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
        handler.postDelayed(carouselRunnable, AUTO_SCROLL_DELAY);

        // Pause auto-scroll when user interacts
        carouselViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(carouselRunnable);
                handler.postDelayed(carouselRunnable, AUTO_SCROLL_DELAY);
            }
        });
    }

    private List<Product> getSampleProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("1", "Shirt", 29.99, "https://placecats.com/millie/300/150", "Men", "Hello"));
        list.add(new Product("2", "Dress", 49.99, "https://placecats.com/leo/300/150", "Women", "Hello"));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(carouselRunnable);
    }
}