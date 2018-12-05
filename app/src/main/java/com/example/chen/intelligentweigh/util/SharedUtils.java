package com.example.chen.intelligentweigh.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * author : chen
 * date   : 2018/11/29  15:32
 * desc   :
 */
public class SharedUtils {

    private Context context ;

    public static  String getPhone(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        return phone;
    }

    public static String getTime(Context context) {
        String phone = getPhone(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(phone,MODE_PRIVATE);
        String time = sharedPreferences.getString("icontime", "");
        return time;
    }
}
