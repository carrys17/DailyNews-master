package com.example.shang.dailynews_master.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shang on 2017/7/26.
 */

public class SpUtils {
    public static void putString(Context context, String key, String responseData){
        SharedPreferences sp = context.getSharedPreferences("dailyNewsConfig", Context.MODE_PRIVATE);
        sp.edit().putString(key, responseData).apply();
    }
    public static String getString(Context context, String key, String defValue){
        SharedPreferences sp = context.getSharedPreferences("dailyNewsConfig", Context.MODE_PRIVATE);
        String string = sp.getString(key, defValue);
        return string;
    }

//    public static void putBoolean(Context context, String key, boolean responseData){
//        SharedPreferences sp = context.getSharedPreferences("dailyNewsConfig", Context.MODE_PRIVATE);
//        sp.edit().putBoolean(key, responseData).apply();
//    }
//    public static boolean getBoolean(Context context, String key, boolean defValue){
//        SharedPreferences sp = context.getSharedPreferences("dailyNewsConfig", Context.MODE_PRIVATE);
//        boolean flag = sp.getBoolean(key, defValue);
//        return flag;
//    }
}
