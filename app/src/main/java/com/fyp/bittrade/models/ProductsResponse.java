package com.fyp.bittrade.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsResponse {

    @SerializedName("products")
    private List<Product> productList;

    @SerializedName("current")
    private int currentPageNumber;

    @SerializedName("pages")
    private int totalPages;

    @SerializedName("success")
    private boolean isSuccess;

    @SerializedName("message")
    private String message;

    public ProductsResponse(List<Product> productList, int currentPageNumber, int totalPages) {
        this.productList = productList;
        this.currentPageNumber = currentPageNumber;
        this.totalPages = totalPages;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
