package com.fyp.bittrade.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fyp.bittrade.models.User;

public class PreferenceUtil {

    public PreferenceUtil() {
    }

    public static boolean saveUser(User user, String password, Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", user.getEmail());
        editor.putString("password", password);
        editor.putString("username", user.getUsername());
        editor.putString("message", user.getMessage());
        editor.putBoolean("isSuccess", user.isSuccess());
        editor.putString("id", user.getId());
        editor.apply();

        return true;
    }

    public static User getUser(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        User user = new User(preferences.getString("email", null), null);
        user.setUsername(preferences.getString("username", null));
        user.setMessage(preferences.getString("message", null));
        user.setSuccess(preferences.getBoolean("isSuccess", false));
        user.setId(preferences.getString("id", null));

        if (user.getEmail() == null || user.getUsername() == null || user.getId() == null) {
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
        editor.putString("username", null);
        editor.putString("message", null);
        editor.putBoolean("isSuccess", false);
        editor.putString("id", null);
        editor.apply();
    }
}
