package com.example.myapplication.models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> categoryList = new MutableLiveData<>(new ArrayList<>());
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "CategoryPrefs";
    private static final String KEY_CATEGORIES = "categories";

    public CategoryViewModel(Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadCategories();
    }

    private void loadCategories() {
        Set<String> categorySet = sharedPreferences.getStringSet(KEY_CATEGORIES, new HashSet<>());
        List<String> initialList = new ArrayList<>(categorySet);
        if (initialList.isEmpty()) {
            initialList.add("Men");
            initialList.add("Women");
            initialList.add("Kids");
            saveCategories(initialList);
        }
        categoryList.setValue(initialList);
    }

    private void saveCategories(List<String> categories) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_CATEGORIES, new HashSet<>(categories));
        editor.apply();
    }

    public LiveData<List<String>> getCategoryList() {
        return categoryList;
    }

    public void addCategory(String category) {
        List<String> currentList = categoryList.getValue();
        if (currentList != null && !currentList.contains(category)) {
            currentList.add(category);
            categoryList.setValue(currentList);
            saveCategories(currentList);
        }
    }

    public void removeCategory(String category) {
        List<String> currentList = categoryList.getValue();
        if (currentList != null) {
            currentList.remove(category);
            categoryList.setValue(currentList);
            saveCategories(currentList);
        }
    }
}