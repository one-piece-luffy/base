package com.baofu.base.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class CommonSharePreference {


    public static void saveUUID(Context context,String boo) {

        SharedPreferences mSharedPreferences =context.getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("common_app_uuid", boo);
        editor.apply();
    }

    public static String getUUID(Context context) {
        SharedPreferences sp = context.getSharedPreferences("app", Context.MODE_PRIVATE);
        String result = sp.getString("common_app_uuid", null);
        return result;

    }


}
