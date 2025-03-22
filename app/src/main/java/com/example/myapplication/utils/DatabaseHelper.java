package com.example.myapplication.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.models.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "ShopDB";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_NAME + " TEXT NOT NULL,"
                    + COLUMN_PRICE + " REAL NOT NULL,"
                    + COLUMN_IMAGE_URL + " TEXT,"
                    + COLUMN_CATEGORY + " TEXT NOT NULL,"
                    + COLUMN_DESCRIPTION + " TEXT)";
            db.execSQL(CREATE_TABLE);
            Log.d(TAG, "Database table created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating table: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            onCreate(db);
            Log.d(TAG, "Database upgraded from version " + oldVersion + " to " + newVersion);
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            onCreate(db);
            Log.d(TAG, "Database downgraded from version " + oldVersion + " to " + newVersion);
        } catch (Exception e) {
            Log.e(TAG, "Error downgrading database: " + e.getMessage(), e);
            throw e;
        }
    }

    public void addProduct(String id, String name, double price, String imageUrl, String category, String description) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, id);
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_PRICE, price);
            values.put(COLUMN_IMAGE_URL, imageUrl);
            values.put(COLUMN_CATEGORY, category);
            values.put(COLUMN_DESCRIPTION, description);

            Log.d(TAG, "Inserting product with values: " + values.toString());

            long result = db.insert(TABLE_PRODUCTS, null, values);
            if (result == -1) {
                throw new SQLiteException("Failed to insert product into database. Values: " + values.toString());
            }
            Log.d(TAG, "Product inserted successfully: ID=" + id);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error inserting product: " + e.getMessage(), e);
            throw e;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product(
                            cursor.getString(0), // id
                            cursor.getString(1), // name
                            cursor.getDouble(2), // price
                            cursor.getString(3), // imageUrl
                            cursor.getString(4), // category
                            cursor.getString(5)  // description
                    );
                    productList.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return productList;
    }

    public void deleteProduct(String productId) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int rowsDeleted = db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{productId});
            if (rowsDeleted > 0) {
                Log.d(TAG, "Product deleted successfully: ID=" + productId);
            } else {
                Log.w(TAG, "No product found with ID=" + productId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deleting product: " + e.getMessage(), e);
            throw e;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // New method to get a product by ID
    public Product getProductById(String productId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Product product = null;
        try {
            db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + " = ?";
            cursor = db.rawQuery(selectQuery, new String[]{productId});

            if (cursor.moveToFirst()) {
                product = new Product(
                        cursor.getString(0), // id
                        cursor.getString(1), // name
                        cursor.getDouble(2), // price
                        cursor.getString(3), // imageUrl
                        cursor.getString(4), // category
                        cursor.getString(5)  // description
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving product with ID " + productId + ": " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return product;
    }

    // New method to update a product
    public void updateProduct(String id, String name, double price, String imageUrl, String category, String description) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_PRICE, price);
            values.put(COLUMN_IMAGE_URL, imageUrl);
            values.put(COLUMN_CATEGORY, category);
            values.put(COLUMN_DESCRIPTION, description);

            Log.d(TAG, "Updating product with ID " + id + " with values: " + values.toString());

            int rowsUpdated = db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{id});
            if (rowsUpdated > 0) {
                Log.d(TAG, "Product updated successfully: ID=" + id);
            } else {
                throw new SQLiteException("Failed to update product. No product found with ID: " + id);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error updating product: " + e.getMessage(), e);
            throw e;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}