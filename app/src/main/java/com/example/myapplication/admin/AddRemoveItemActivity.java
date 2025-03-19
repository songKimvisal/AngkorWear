package com.example.myapplication.admin;

import android.content.Intent;
import android.graphics.Bitmap;
// import android.graphics.drawable.BitmapDrawable; // Not directly used in the current code
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AddRemoveItemActivity extends AppCompatActivity {

    private EditText editTextItemName, editTextDescription, editTextPrice;
    private Button buttonSelectItemImage, buttonAddItem, buttonRemoveItem;
    private ImageView imageViewItem; // Displays the selected image
    private DatabaseHelper dbHelper;
    private Uri selectedImageUri; // Holds the URI of the selected image
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_remove_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonSelectItemImage = findViewById(R.id.buttonSelectItemImage); // Button to trigger image selection
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonRemoveItem = findViewById(R.id.buttonRemoveItem);
        imageViewItem = findViewById(R.id.imageViewItem); // ImageView to display the selected image

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set up listeners
        buttonSelectItemImage.setOnClickListener(v -> openFileChooser());

        buttonAddItem.setOnClickListener(v -> addItemToDatabase());

        buttonRemoveItem.setOnClickListener(v -> removeItemFromDatabase());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Request code for image selection activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUri = data.getData(); // URI of the image selected by the user
            imageViewItem.setImageURI(selectedImageUri); // Display the selected image in the ImageView
            imageViewItem.setVisibility(View.VISIBLE);
        }
    }

    private void addItemToDatabase() {
        String name = editTextItemName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Please enter item name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = null;
        if (selectedImageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri); // Get Bitmap from the selected image URI
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Compress the Bitmap into PNG format
                imageBytes = outputStream.toByteArray(); // Convert the compressed image to byte array for database storage
            } catch (IOException e) {
                Toast.makeText(this, "Error converting image", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
        } else {
            // Handle case where no image is selected (you might want to allow this or show a warning)
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ID for the product
        String productId = UUID.randomUUID().toString();

        // Assuming you want to set a default category for now
        String category = "Default";

        dbHelper.addProduct(productId, name, price, imageBytes, category); // Add the product to the database, including the image byte array
        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
        clearInputFields();
    }

    private void removeItemFromDatabase() {
        String itemNameToRemove = editTextItemName.getText().toString().trim();

        if (TextUtils.isEmpty(itemNameToRemove)) {
            Toast.makeText(this, "Please enter the name of the item to remove", Toast.LENGTH_SHORT).show();
            return;
        }

        int rowsAffected = dbHelper.deleteProductByName(itemNameToRemove);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Item removed successfully", Toast.LENGTH_SHORT).show();
            clearInputFields();
            imageViewItem.setVisibility(View.GONE); // Hide the image view after removal
            selectedImageUri = null; // Clear the selected image URI
        } else {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        editTextItemName.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        imageViewItem.setVisibility(View.GONE); // Hide the image view
        selectedImageUri = null; // Clear the selected image URI
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}