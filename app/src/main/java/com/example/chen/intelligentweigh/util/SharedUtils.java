package com.example.chen.intelligentweigh.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chen.intelligentweigh.bean.User;

import org.litepal.LitePal;

import java.util.List;

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

    public static User getMyInfo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        List<User> users = LitePal.where("phone = ?", phone).find(User.class);
        if(!users.isEmpty()){
            return users.get(0);
        }else{
            return null;
        }
    }

    public static String getUserFarmId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        List<User> users = LitePal.where("phone = ?", phone).find(User.class);
        if(!users.isEmpty()){
            return users.get(0).getFarmid();
        }else{
            return null;
        }
    }


    public static String getUserFarms(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        List<User> users = LitePal.where("phone = ?", phone).find(User.class);
        if(!users.isEmpty()){
            return users.get(0).getFarmids();
        }else{
            return null;
        }
    }
}
