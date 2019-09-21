package com.fyp.bittrade.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fyp.bittrade.models.Contact;
import com.fyp.bittrade.models.User;

public class PreferenceUtil {

    public PreferenceUtil() {
    }

    public static boolean saveUser(User user, String password, Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", user.getEmail());
        editor.putString("password", password);
        editor.putString("firstName", user.getFirstName());
        editor.putString("lastName", user.getLastName());
        editor.putString("id", user.getId());
        editor.apply();

        return true;
    }

    public static boolean saveImageUrl(String imageUrl, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("imageUrl", imageUrl);
        editor.apply();

        return true;
    }

    public static boolean saveAddress(String address, String city, String zip, String country, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("address", address);
        editor.putString("city", city);
        editor.putString("zip", zip);
        editor.putString("country", country);
        editor.apply();

        return true;
    }

    public static Contact getAddress(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        Contact contact = new Contact();

        contact.setAddress(preferences.getString("address", null));
        contact.setCity(preferences.getString("city", null));
        contact.setZip(preferences.getString("zip", null));
        contact.setCountry(preferences.getString("country", null));

        return contact;
    }

    public static boolean savePhone(String phone, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("phone", phone);
        editor.apply();

        return true;
    }

    public static String getPhone(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ph = preferences.getString("phone", null);

        return ph;
    }

    public static String getImageUrl(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String str = preferences.getString("imageUrl", null);
        return str;
    }

    public static User getUser(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        User user = new User(preferences.getString("email", null), null);
        user.setFirstName(preferences.getString("firstName", null));
        user.setLastName(preferences.getString("lastName", null));
        user.setId(preferences.getString("id", null));

        if (user.getEmail() == null || user.getFirstName() == null || user.getLastName() == null || user.getId() == null) {
            user = null;
        }

        return user;
    }

//    public static String getUserPassword(String email, Context context) {
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getString(email, null);
//    }

    public static void deleteUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", null);
        editor.putString("password", null);
        editor.putString("firstName", null);
        editor.putString("lastName", null);
        editor.putString("id", null);
        editor.putString("imageUrl", null);
        editor.apply();
    }
}
