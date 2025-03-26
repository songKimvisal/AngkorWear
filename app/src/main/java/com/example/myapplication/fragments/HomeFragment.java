package com.example.myapplication.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager; // Changed to GridLayoutManager
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
        setupRecyclerView();

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

        // Connect TabLayout with ViewPager2 and improve accessibility
        new TabLayoutMediator(carouselIndicators, carouselViewPager, (tab, position) -> {
            tab.setContentDescription("Slide " + (position + 1) + ": Carousel Image");
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

    private void setupRecyclerView() {
        // Use GridLayoutManager with 2 columns
        productsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        productsRecyclerView.setNestedScrollingEnabled(false); // Ensure NestedScrollView handles scrolling
        List<Product> productList = getSampleProducts();
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
        productsRecyclerView.setAdapter(productAdapter);
    }

    private List<Product> getSampleProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("1", "Shirt", 59.99, "https://shoparitmetik.com/cdn/shop/files/selected-slim-fit-shirt-navy-selected-shirt-shop-namemontreal-224052.jpg?v=1729703414", "Men", "SELECTED - SLIM FIT SHIRT - NAVY"));
        list.add(new Product("2", "Shirt", 49.99, "https://shoparitmetik.com/cdn/shop/files/selected-slim-fit-shirt-beige-selected-shirt-shop-namemontreal-121722_460x.jpg?v=1730863493", "Men", "SELECTED - SLIM FIT SHIRT - BEIGE"));
        list.add(new Product("3", "Dress", 79.99, "https://mahezon.in/cdn/shop/files/IMG-20240527_221759_161_500x641_crop_center.jpg?v=1716829415", "Women", "Party Wear Women Black Dress Floral Women Midi Frock Dress"));
        list.add(new Product("4", "Jacket", 89.99, "https://www.side-step.co.za/media/catalog/product/cache/ead79d362eadd98297170252f181cb50/v/l/vlt26b-voltage-parka-black-volw24-0016a-v1_jpg.jpg", "Men", "Voltage Mens Parka Jacket Black"));
        list.add(new Product("5", "Pants", 39.99, "https://amaioofficial.com/cdn/shop/files/A7a043f4d30bc4a93bf39310eaa72aa18w.webp?v=1705740268", "Women", "Women Y2K Cargo Pants High Waist Streetwear Hip Hop Trousers"));
        list.add(new Product("6", "Sweater", 69.99, "https://assets.ajio.com/medias/sys_master/root/h20/hea/14858211754014/-473Wx593H-460439786-lightpink-MARKETING.jpg", "Women", "Knitted High-Neck Pullover with Drop-Shoulders"));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(carouselRunnable);
    }
}