package com.example.myapplication.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.utils.DatabaseHelper;

import java.io.ByteArrayOutputStream;

public class AdminActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText nameInput, priceInput, categoryInput;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        nameInput = findViewById(R.id.name_input);
        priceInput = findViewById(R.id.price_input);
        categoryInput = findViewById(R.id.category_input);
        Button pickImageButton = findViewById(R.id.pick_image_button);
        Button saveButton = findViewById(R.id.save_button);

        pickImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        saveButton.setOnClickListener(v -> {
            String id = String.valueOf(System.currentTimeMillis());
            String name = nameInput.getText().toString();
            double price = Double.parseDouble(priceInput.getText().toString());
            String category = categoryInput.getText().toString();
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.addProduct(id, name, price, imageBytes, category);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}