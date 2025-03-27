package com.example.myapplication.models;

import android.os.Parcelable;
import android.os.Parcel;

public class CartItem implements Parcelable {
    private String name;
    private double price;
    private int quantity;
    private String size;

    public CartItem(String name, double price, int quantity,String size) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {return size;}
    // Setter for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Parcelable implementation
    protected CartItem(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        size = in.readString();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}