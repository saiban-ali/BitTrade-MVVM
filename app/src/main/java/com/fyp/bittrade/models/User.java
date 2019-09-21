package com.fyp.bittrade.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User implements Parcelable {

    /**
     * {
     *    "success": true,
     *    "message": "Sucessfully Edited the Address data",
     *    "data": {
     *        "email_verified": false,
     *        "addresses_isAvailavle": false,
     *        "phone_verified": false,
     *        "payment_method_added": false,
     *        "_id": "5d80866e73c8f529c0e47244",
     *        "first_name": "zahid",
     *        "last_name": "Tariq",
     *        "email": "zahid@facebook.com",
     *        "password": "zahid123",
     *        "payment_method": [],
     *        "createdAt": "2019-09-17T07:08:30.162Z",
     *        "updatedAt": "2019-09-17T07:08:30.162Z",
     *        "__v": 0
     *    }
     * }
     **/

    // TODO: 9/17/2019 Payment Method Model


    @SerializedName("_id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("profile_image")
    private String profileImageUrl;

    @SerializedName("phone")
    private String phoneNumber;

    @SerializedName("address")
    private Contact contact;

    @SerializedName("payment_method_added")
    private boolean isPaymentMethodAvailable;

    @SerializedName("phone_verified")
    private boolean isPhoneVarified;

    @SerializedName("addresses_is_availavle")
    private boolean isAddressAvailable;

    @SerializedName("email_verified")
    private boolean isEmailVarified;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    protected User(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        password = in.readString();
        profileImageUrl = in.readString();
        phoneNumber = in.readString();
        isPaymentMethodAvailable = in.readByte() != 0;
        isPhoneVarified = in.readByte() != 0;
        isAddressAvailable = in.readByte() != 0;
        isEmailVarified = in.readByte() != 0;
        createdAt = in.readString();
        updatedAt = in.readString();
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

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isPaymentMethodAvailable() {
        return isPaymentMethodAvailable;
    }

    public boolean isPhoneVarified() {
        return isPhoneVarified;
    }

    public boolean isAddressAvailable() {
        return isAddressAvailable;
    }

    public boolean isEmailVarified() {
        return isEmailVarified;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(profileImageUrl);
        dest.writeString(phoneNumber);
        dest.writeByte((byte) (isPaymentMethodAvailable ? 1 : 0));
        dest.writeByte((byte) (isPhoneVarified ? 1 : 0));
        dest.writeByte((byte) (isAddressAvailable ? 1 : 0));
        dest.writeByte((byte) (isEmailVarified ? 1 : 0));
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }
}
