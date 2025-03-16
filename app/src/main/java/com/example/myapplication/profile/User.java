package com.example.myapplication.profile;


public class User {
    private String username;
    private String email;
    private String password;  // Add password field

    // Modify constructor to accept username, email, and password
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;  // Initialize password
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;  // Add getter for password
    }
}

