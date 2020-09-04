package com.khojokhao.Constant;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    public static String SharedPref = "SharedPref";
    public static String isLogin = "isLogin";
    public static String customer_id = "customer_id";
    public static String customer_unique_id = "customer_unique_id";
    public static String mobile_no = "mobile_no";
    public static String email_id = "email_id";
    public static String customer_img = "customer_img";
    public static String customer_name = "customer_name";
    public static String token = "token";
    public static String Address = "Address";
    public static String referral_code = "referral_code";
    public static String fran_code = "fran_code";
    public static String appExit = "appExit";
    public static String pincodeChecked = "pincodeChecked";
//    public static String Address2 = "address2";
//    public static String State1 = "state1";
//    public static String State2 = "state2";
    /*public static String Password = "password";
    public static String Token = "token"*/;

    public static void putBol(Context context, String key, boolean val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static boolean getBol(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static String getVal(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void putVal(Context context, String key, String val) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static void clearData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


}
