package com.abdotareq.subway_e_ticketing.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A util class that contains a global static methods for SharedPreference Management used across the App
 */
public class SharedPreferenceUtil {

    public final static String LOGGED_IN_FLAG = "LOGGED_IN_FLAG";
    public final static String USER_ID_FLAG = "USER_ID_FLAG";
    public final static String USER_NAME_FLAG = "USER_NAME_FLAG";

    public final static String USER_NIGHT_MODE = "USER_NIGHT_MODE";

    public static void setSharedPrefsLoggedIn(Context context, boolean isChecked) {
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(LOGGED_IN_FLAG, isChecked);
        editor.apply();

    }

    public static boolean getSharedPrefsLoggedIn(Context context) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        return settings.getBoolean(LOGGED_IN_FLAG, false);
    }

    public static void setSharedPrefsNightMode(Context context, boolean isChecked) {
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(USER_NIGHT_MODE, isChecked);
        editor.apply();

    }

    public static boolean getSharedPrefsNightMode(Context context) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        return settings.getBoolean(USER_NIGHT_MODE, false);
    }

    //write token into SharedPreferences to use in remember user
    public static void setSharedPrefsTokenId(Context context, String token) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_ID_FLAG, token);
        editor.apply();

    }

    //get token from SharedPreferences to use in remember user
    public static String getSharedPrefsTokenId(Context context) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        return settings.getString(USER_ID_FLAG, "-1");

    }

    //write token into SharedPreferences to use in remember user
    public static void setSharedPrefsName(Context context, String name) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_NAME_FLAG, name);
        editor.apply();

    }

    //get token from SharedPreferences to use in remember user
    public static String getSharedPrefsName(Context context) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        return settings.getString(USER_NAME_FLAG, "");

    }

}
