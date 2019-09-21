package com.fyp.bittrade.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private boolean isSuccess;

    @SerializedName("data")
    private User user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
