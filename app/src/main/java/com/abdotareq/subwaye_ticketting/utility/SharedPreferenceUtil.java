package com.abdotareq.subwaye_ticketting.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A util class that contains a global static methods for SharedPreference Management used across the App
 */
public class SharedPreferenceUtil {

    public final static String LOGGED_IN_FLAG = "LOGGED_IN_FLAG";
    public final static String USER_ID_FLAG = "USER_ID_FLAG";

    //write token into SharedPreferences to use in remember user
    public static void setSharedPrefsLoggedIn(Context context, boolean isChecked) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(LOGGED_IN_FLAG, isChecked);
        editor.apply();

    }

    //get token from SharedPreferences to use in remember user
    public static boolean getSharedPrefsLoggedIn(Context context) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        return settings.getBoolean(LOGGED_IN_FLAG, false);
    }

    public static void setSharedPrefsUserId(Context context, String token) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_ID_FLAG, token);
        editor.apply();

    }

    public static long getSharedPrefsUserId(Context context) {

        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        return settings.getLong(USER_ID_FLAG, -1);

    }

}
