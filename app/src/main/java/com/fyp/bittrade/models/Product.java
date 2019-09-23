package com.fyp.bittrade.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {

    public static final int HAS_NO_IMAGE_URL = 0;

    @SerializedName("_id")
    private String id;

    public String getId() {
        return id;
    }

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

    @SerializedName("quantity")
    private int productCount;

    @SerializedName("category")
    private String category;

    @SerializedName("brand")
    private String brand;

    public Product() {
    }

    public Product(String[] images, String title, String description, double price, int stock, int productCount) {
        this.images = images;
        this.title = title;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.productCount = productCount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public void setImages(String[] images) {
        this.images = images;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void incrementProductCount() {
        productCount++;
    }

    public void decrementProductCount() {
        productCount--;
    }


    protected Product(Parcel in) {
        id = in.readString();
        images = in.createStringArray();
        title = in.readString();
        description = in.readString();
        price = in.readDouble();
        stock = in.readInt();
        productCount = in.readInt();
        category = in.readString();
        brand = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringArray(images);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(stock);
        dest.writeInt(productCount);
        dest.writeString(category);
        dest.writeString(brand);
    }

    @Override
    public int describeContents() {
        return 0;
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

}
