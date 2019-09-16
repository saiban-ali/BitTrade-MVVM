package com.fyp.bittrade.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartListResponse {

    @SerializedName("products")
    private List<Product> productList;

    @SerializedName("success")
    private boolean isSuccess;

    @SerializedName("message")
    private String message;

    public List<Product> getProductList() {
        return productList;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public double getTotalPrice() {
        double price = 0;
        for (Product p :
                productList) {
            price += p.getPrice();
        }
        return price;
    }
}
