package com.chatnovel.whitewhale.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by Wyatt on 2017/6/10/010.
 */

public class WWSharePreference {


    // 设置SharedPreferences值--字符串类型
    public static void setSharedPreferencesValueToString(String keyName,
                                                         String keyValue, Context context) {
        // 取得SharedPreferences对象
        SharedPreferences getPrefer_String = context.getSharedPreferences(
                keyName, Context.MODE_PRIVATE);
        // 取得可编辑对象
        SharedPreferences.Editor editor_String = getPrefer_String.edit();
        // 添加值
        editor_String.putString(keyName, keyValue);
        // 保存提交
        editor_String.commit();
//        DiaoBaoBaseSharedPreferences.commitSharedPreferences(editor_String);
    }

    // 设置SharedPreferences值--int类型
    public static void setSharedPreferencesValueToInt(String keyName,
                                                      int keyValue, Context context) {
        // 取得SharedPreferences对象
        SharedPreferences getPrefer_Int = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        // 取得可编辑对象
        SharedPreferences.Editor editor_Int = getPrefer_Int.edit();
        // 添加值
        editor_Int.putInt(keyName, keyValue);
        // 保存提交
        commitSharedPreferences(editor_Int);
    }

    public static void setSharedPreferencesValueToLong(String keyName,
                                                       long keyValue, Context context) {
        // 取得SharedPreferences对象
        SharedPreferences getPrefer_Int = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        // 取得可编辑对象
        SharedPreferences.Editor editor_Int = getPrefer_Int.edit();
        // 添加值
        editor_Int.putLong(keyName, keyValue);
        // 保存提交
        commitSharedPreferences(editor_Int);
    }

    public static void setSharedPreferencesValueToFloat(String keyName,
                                                        float keyValue, Context context) {
        // 取得SharedPreferences对象
        SharedPreferences getPrefer_Int = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        // 取得可编辑对象
        SharedPreferences.Editor editor_Int = getPrefer_Int.edit();
        // 添加值
        editor_Int.putFloat(keyName, keyValue);
        // 保存提交
        commitSharedPreferences(editor_Int);
    }

    public static void setSharedPreferencesValueToBoolean(String keyName,
                                                          boolean keyValue, Context context) {
        // 取得SharedPreferences对象
        SharedPreferences getPrefer_Boolean = context.getSharedPreferences(
                keyName, Context.MODE_PRIVATE);
        // 取得可编辑对象
        SharedPreferences.Editor editor_String = getPrefer_Boolean.edit();
        // 添加值
        editor_String.putBoolean(keyName, keyValue);
        // 保存提交
        editor_String.apply();
    }

    // 获取SharedPreferences值
    public static String getSharedPreferencesValueToString(String keyName,
                                                           Context context, String str) {
        // 获取值
        SharedPreferences sharPrefer = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        String value = sharPrefer.getString(keyName, str);

        return value;
    }

    public static int getSharedPreferencesValueToInt(String keyName,
                                                     Context context, int inte) {
        // 获取值
        SharedPreferences sharPrefer = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        int value = sharPrefer.getInt(keyName, inte);
        return value;
    }

    public static long getSharedPreferencesValueToLong(String keyName,
                                                       Context context, long inte) {
        // 获取值sp_push_broadcast
        SharedPreferences sharPrefer = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        long value = sharPrefer.getLong(keyName, inte);
        return value;
    }

    public static float getSharedPreferencesValueToFloat(String keyName,
                                                         Context context, float inte) {
        // 获取值
        SharedPreferences sharPrefer = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        float value = sharPrefer.getFloat(keyName, inte);
        return value;
    }

    public static boolean getSharedPreferencesValueToBoolean(String keyName,
                                                             Context context, boolean inte) {
        // 获取值
        SharedPreferences sharPrefer = context.getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        boolean value = sharPrefer.getBoolean(keyName, inte);
        return value;
    }

    public static void clear(String keyName,Context context)
    {
        // 取得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences(
                keyName, Context.MODE_PRIVATE);
        // 取得可编辑对象
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        commitSharedPreferences(editor);
    }





    public static void commitSharedPreferences(android.content.SharedPreferences.Editor editor){
        if(getAndroidSDKVersion() > Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }
        else{
            editor.commit();
        }
    }
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {

        }
        return version;
    }
}
