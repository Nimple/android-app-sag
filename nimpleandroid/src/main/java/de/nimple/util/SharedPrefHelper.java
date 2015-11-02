package de.nimple.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefHelper {
    public static boolean exists(String key, Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.contains(key);
    }

    public static void remove(String key, Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        prefs.edit().remove(key).apply();
    }

    public static void putString(String key, String value, Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        prefs.edit().putString(key, value).apply();
    }

    public static String getString(String key, Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(key, "");
    }

    public static void putInt(String key, int value, Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        prefs.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, Context ctx){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getInt(key,0);
    }

    public static void putBoolean(String key, boolean value, Context ctx) {
        if(ctx == null || key == null ){
            Lg.d("SharedPreHelper:putBoolean:Context or key is empty");
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        prefs.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, Context ctx) {
        if(ctx == null || key == null ){
            Lg.d("SharedPreHelper:getBoolean:Context or key is empty");
            return false;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue, Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(key, defValue);
    }
}