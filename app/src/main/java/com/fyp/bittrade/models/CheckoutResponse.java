package com.fyp.bittrade.models;

import com.google.gson.annotations.SerializedName;

public class CheckoutResponse {

    @SerializedName("hosted_url")
    private String url;

    @SerializedName("success")
    private boolean isSuccessful;

    @SerializedName("message")
    private String message;

    public String getUrl() {
        return url;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getMessage() {
        return message;
    }
}
