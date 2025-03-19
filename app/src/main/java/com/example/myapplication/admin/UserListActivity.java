package com.example.myapplication.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.profile.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private DatabaseHelper databaseHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbarWithBackArrow);
        setSupportActionBar(toolbar);

        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        List<User> userList = getAllUsers();

        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = databaseHelper.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password"))
                );
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        private List<User> users;

        public UserAdapter(List<User> users) {
            this.users = users;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_item, parent, false);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            User user = users.get(position);
            holder.usernameTextView.setText(user.getUsername());
            holder.emailTextView.setText(user.getEmail());

            holder.deleteButton.setOnClickListener(v -> {
                deleteUser(user.getEmail());
                users.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, users.size());
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public class UserViewHolder extends RecyclerView.ViewHolder {
            public TextView usernameTextView;
            public TextView emailTextView;
            public Button deleteButton;

            public UserViewHolder(View view) {
                super(view);
                usernameTextView = view.findViewById(R.id.usernameTextView);
                emailTextView = view.findViewById(R.id.emailTextView);
                deleteButton = view.findViewById(R.id.deleteButton);
            }
        }
    }

    private void deleteUser(String email) {
        android.database.sqlite.SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsAffected = db.delete("users", "email = ?", new String[]{email});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show();
        }
    }
}