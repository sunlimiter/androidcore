package com.tywho.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

/**
 * Created by limit on 2017/3/29.
 */

public class SharePreUtil {
    private static String PRENAME = "app_setting";//默认文件名

    /**
     * 删除键值对
     */
    public static void removeKey(String preName, Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void removeKey(Context context, String key) {
        removeKey(PRENAME, context, key);
    }

    /**
     * 清空
     *
     * @param context
     */
    public static void clear(Context context) {
        clear(PRENAME, context);
    }

    public static void clear(String preName, Context context) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.clear().commit();
    }

    /**
     * 从配置文件读取字符串
     *
     * @param preName 配置文件名
     * @param key     字符串键值
     * @return 键值对应的字符串, 默认返回""
     */
    public static String getString(String preName, Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getString(key, "");
    }

    public static String getString(Context context, String key) {
        return getString(PRENAME, context, key);
    }

    /**
     * 从配置文件读取int数据
     *
     * @param preName 配置文件名
     * @param key     int的键值
     * @return 键值对应的int, 默认返回-1
     */
    public static int getInt(String preName, Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getInt(key, -1);
    }

    public static int getInt(Context context, String key) {
        return getInt(PRENAME, context, key);
    }

    /**
     * 从配置文件读取Boolean值
     *
     * @return 如果没有，默认返回false
     */
    public static Boolean getBoolean(String preName, Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getBoolean(key, false);
    }

    public static Boolean getBoolean(Context context, String key) {
        return getBoolean(PRENAME, context, key);
    }

    /**
     * 从配置文件获取float数据
     *
     * @return 默认返回0.0f
     */
    public static float getFloat(String preName, Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getFloat(key, 0.0f);
    }

    public static float getFloat(Context context, String key) {
        return getFloat(PRENAME, context, key);
    }

    /**
     * 从配置文件获取对象
     */
    public static <T> T getObject(String preName, Context context, String key, Class<T> clazz) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        String str = pre.getString(key, "");
        return TextUtils.isEmpty(str) ? null : JsonUtils.fromJson(str, clazz);
    }

    public static <T> T getObject(Context context, String key, Class<T> clazz) {
        return getObject(PRENAME, context, key, clazz);
    }

    /**
     * 从配置文件获取byte数组
     */
    public static byte[] getByteArr(String preName, Context context, String key) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        String str = pre.getString(key, "");
        return Base64.decode(str.getBytes(), Base64.DEFAULT);
    }

    public static byte[] getByteArr(Context context, String key) {
        return getByteArr(PRENAME, context, key);
    }

    /**
     * 存储字符串到配置文件
     *
     * @param preName 配置文件名
     * @param key     存储的键值
     * @param value   需要存储的字符串
     * @return 成功标志
     */
    public static Boolean putString(String preName, Context context, String key, String value) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static Boolean putString(Context context, String key, String value) {
        return putString(PRENAME, context, key, value);
    }

    /**
     * 保存Float数据到配置文件
     */
    public static Boolean putFloat(String preName, Context context, String key, float value) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static Boolean putFloat(Context context, String key, float value) {
        return putFloat(PRENAME, context, key, value);
    }

    /**
     * 存储数字到配置文件
     *
     * @param preName 配置文件名
     * @param key     存储的键值
     * @param value   需要存储的数字
     * @return 成功标志
     */
    public static Boolean putInt(String preName, Context context, String key, int value) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static Boolean putInt(Context context, String key, int value) {
        return putInt(PRENAME, context, key, value);
    }

    /**
     * 存储Boolean值到配置文件
     *
     * @param preName 配置文件名
     * @param key     键值
     * @param value   需要存储的boolean值
     */
    public static Boolean putBoolean(String preName, Context context, String key, Boolean value) {
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static Boolean putBoolean(Context context, String key, Boolean value) {
        return putBoolean(PRENAME, context, key, value);
    }

    /**
     * 存放对象
     */
    public static Boolean putObject(String preName, Context context, String key, Class<?> clazz,
                                    Object obj) {
        String str = new Gson().toJson(obj, clazz);
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(key, str);
        return editor.commit();
    }

    public static Boolean putObject(Context context, String key, Class<?> clazz, Object obj) {
        return putObject(PRENAME, context, key, clazz, obj);
    }

    /**
     * 存放byte数组
     */
    public static Boolean putByteArr(String preName, Context context, String key, byte[] bytes) {
        String str = StringUtils.isEmpty(bytes) ? null : new String(Base64.encode(bytes, Base64.DEFAULT));
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(key, str);
        return editor.commit();
    }

    public static Boolean putByteArr(Context context, String key, byte[] bytes) {
        return putByteArr(PRENAME, context, key, bytes);
    }
}
