package com.dq.huifenbao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus on 2018/3/31.
 */

public class MySharedPreferences {
    //创建一个SharedPreferences    类似于创建一个数据库，库名为 data
    public static SharedPreferences share(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("date",
                Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    //账号1
    public static String getPhone1(Context context){
        return share(context).getString("phone1",null);
    }
    //这里使用的是 apply() 方法保存，将不会有返回值
    public static void setPhone1(String phone1, Context context){
        SharedPreferences.Editor e = share(context).edit();
        e.putString("phone1",phone1);
        e.apply();
    }

    //账号2
    public static String getPhone2(Context context){
        return share(context).getString("phone2",null);
    }
    //这里使用的是 apply() 方法保存，将不会有返回值
    public static void setPhone2(String phone2, Context context){
        SharedPreferences.Editor e = share(context).edit();
        e.putString("phone2",phone2);
        e.apply();
    }

    //账号3
    public static String getPhone3(Context context){
        return share(context).getString("phone3",null);
    }
    //这里使用的是 apply() 方法保存，将不会有返回值
    public static void setPhone3(String phone3, Context context){
        SharedPreferences.Editor e = share(context).edit();
        e.putString("phone3",phone3);
        e.apply();
    }
}