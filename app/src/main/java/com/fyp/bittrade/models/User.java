package com.fyp.bittrade.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String username;

    @SerializedName("email")
    private String email;

    private String password;

    @SerializedName("message")
    private String message;

    @SerializedName("Success")
    private boolean success;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    protected User(Parcel in) {
        username = in.readString();
        email = in.readString();
        password = in.readString();
        message = in.readString();
        success = in.readByte() != 0;
        id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(message);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(id);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
