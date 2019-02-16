package com.example.rucha.pocketbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {
    static final String PREF_EMAIL = "email";
    static final String LOGGED_IN = "loggedIn";

    static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    //call this method when user logs in to store email
    public static void setEmail(Context ctx, String email)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, email);
        editor.commit();
    }

    public static String getEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }

    //this method to clear all sharedpreferences when user logs out
    public static void clear(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

    //call this method to store that user has logged in
    public static void setLoggedIn(Context ctx, boolean logged)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(LOGGED_IN, true);
        editor.commit();
    }

    //when app reopens call this method to check if user had already logged in
    public static boolean isLoggedIn(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(LOGGED_IN, false);
    }
}
