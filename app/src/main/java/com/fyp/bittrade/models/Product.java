package com.fyp.bittrade.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {

    public static final int HAS_NO_IMAGE_URL = 0;

    @SerializedName("images")
    private String[] images;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName("stock")
    private int stock;

    public Product() {
    }

    public Product(String[] images, String title, String description, double price, int stock) {
        this.images = images;
        this.title = title;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    protected Product(Parcel in) {
        images = in.createStringArray();
        title = in.readString();
        description = in.readString();
        price = in.readDouble();
        stock = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String[] getImages() {
        return images;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(images);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(stock);
    }
}
