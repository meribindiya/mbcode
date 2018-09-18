package com.deecoders.meribindiya.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by saif on 9/21/2016.
 */
public class MyPref {
    private static final String PREFS_NAME = "com.deecoders.meribindiya";

    public static void setId(Context context, String id){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("user_id", id).apply();
    }
    public static String getId(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("user_id", null);
    }

    public static void setEmail(Context context, String email){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("email", email).apply();
    }
    public static String getEmail(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("email", "");
    }

    public static void setMobile(Context context, String mobile){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("mobile", mobile).apply();
    }
    public static String getMobile(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("mobile", "");
    }

    public static void setGender(Context context, String gender){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("gender", gender).apply();
    }
    public static String getGender(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("gender", "female");
    }

    public static void setName(Context context, String name){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("name", name).apply();
    }
    public static String getName(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("name", "");
    }

    public static void setLogin(Context context, int login) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt("isUserlogin", login).apply();
    }
    public static int getLogin(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("isUserlogin", 0);
    }
}
