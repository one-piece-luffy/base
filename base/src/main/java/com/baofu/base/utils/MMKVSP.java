package com.baofu.base.utils;

import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;


import com.alibaba.fastjson.JSON;
import com.baofu.base.BaseApplication;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MMKVSP {

    /**
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {

        return getMMKVInt(context, key, defaultValue);
    }

    /**
     * @return
     */
    public static String getString(Context context, String key, String defaultValue) {

        return getMMKVString(context, key, defaultValue);
    }


    /**
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {

        return getMMKVBoolean(context, key, defaultValue);
    }

    /**
     * @return
     */
    public static float getFloat(Context context, String key, float defaultValue) {

        return getMMKVFloat(context, key, defaultValue);
    }

    /**
     * @return
     */
    public static long getLong(Context context, String key, long defaultValue) {

        return getMMKVLong(context, key, defaultValue);
    }


    /**
     * @return
     */
    public static Set<String> getSetString(Context context, String key) {
        return getMMKVSetString(context, key);
    }

    /**
     * @return
     */
    public static <T extends Parcelable> T getParcelable(Context context, Class<T> cls, String key) {

        return getMMKVParcelable(context, cls, key);
    }


    /**
     * @return
     */
    public static <T> List<T> getList(Context context, Class<T> cls,
                                      String key) {

        return getMMKVList(context, cls, key);
    }


    public static List<String> getAllKey(Context context) {
        return getMMKVAllKey(context);
    }

    /**
     * @return
     */
    public static boolean putInt(Context context, String key, int value) {
        return putMMKVInt(context, key, value);
    }


    /**
     * @return
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        return putMMKVBoolean(context, key, value);
    }


    /**
     * @return
     */

    public static boolean putString(Context context, String key, String value) {
        return putMMKVString(context, key, value);
    }

    /**
     * @return
     */

    public static boolean putSetString(Context context, String key, Set<String> value) {
        return putMMKVSetString(context, key, value);
    }

    /**
     * @return
     */

    public static boolean putFloat(Context context, String key, float value) {
        return putMMKVFloat(context, key, value);
    }


    public static boolean putLong(Context context, String key, long value) {
        return putMMKVLong(context, key, value);
    }

    /**
     * @return
     */

    public static boolean putParcelable(Context context, String key, Parcelable value) {
        return putMMKVParcelable(context, key, value);
    }

    /**
     * @return
     */

    public static void putList(Context context, String key, List value) {
        putMMKVList(context, key, value);
    }

    public static void remove(Context context, String key) {
        // 这里不能异步操作，如果异步，删除完马上调用get，会重新把db的数据赋值到mmkv，导致删除失败
        removeMMKVKey(context, key);
    }


    public static MMKV getMMKV(Context context) {
        MMKV kv = null;
        try {
            kv = MMKV.defaultMMKV();
        } catch (IllegalStateException e) {
            if (BaseApplication.getInstance() != null) {
                MMKV.initialize(BaseApplication.getInstance());
            } else if (context != null && context.getApplicationContext() != null) {
                MMKV.initialize(context.getApplicationContext());
            } else if (context != null) {
                MMKV.initialize(context);
            } else {
                return null;
            }
            kv = MMKV.defaultMMKV();
        }
        return kv;
    }

    public static int getMMKVInt(Context context, String key, int defaultValue) {
        if (getMMKV(context) == null)
            return defaultValue;
        return getMMKV(context).decodeInt(key, defaultValue);
    }

    public static String getMMKVString(Context context, String key, String defaultValue) {
        if (getMMKV(context) == null)
            return defaultValue;
        return getMMKV(context).decodeString(key, defaultValue);
    }

    public static boolean getMMKVBoolean(Context context, String key, boolean defaultValue) {
        if (getMMKV(context) == null)
            return defaultValue;
        return getMMKV(context).decodeBool(key, defaultValue);
    }

    public static float getMMKVFloat(Context context, String key, float defaultValue) {
        if (getMMKV(context) == null)
            return defaultValue;
        return getMMKV(context).decodeFloat(key, defaultValue);
    }

    public static long getMMKVLong(Context context, String key, long defaultValue) {
        if (getMMKV(context) == null)
            return defaultValue;
        return getMMKV(context).decodeLong(key, defaultValue);
    }

    public static double getMMKVDouble(Context context, String key, double defaultValue) {
        if (getMMKV(context) == null)
            return defaultValue;
        return getMMKV(context).decodeDouble(key, defaultValue);
    }

    public static <T extends Parcelable> T getMMKVParcelable(Context context, Class<T> cls,
                                                              String key) {
        if (getMMKV(context) == null)
            return null;
        String json = getMMKVString(context, key, null);
        if (TextUtils.isEmpty(json))
            return null;
        try {
            return JSON.parseObject(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        return getMMKV(context).decodeParcelable(key, cls);
    }


    public static <T> List<T> getMMKVList(Context context, Class<T> cls, String key) {
        String str = getMMKVString(context, key, null);

        if (TextUtils.isEmpty(str))
            return null;
        try {
            return JSON.parseArray(str, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<String> getMMKVSetString(Context context, String key) {
        if (getMMKV(context) == null)
            return null;
        return getMMKV(context).decodeStringSet(key, null);
    }

    public static List<String> getMMKVAllKey(Context context) {
        if (getMMKV(context) == null)
            return null;
        return Arrays.asList(getMMKV(context).allKeys());
    }

    public static boolean putMMKVInt(Context context, String key, int value) {
        if (getMMKV(context) == null)
            return false;
        return getMMKV(context).encode(key, value);
    }


    public static boolean putMMKVBoolean(Context context, String key, boolean value) {
        if (getMMKV(context) == null)
            return false;
        return getMMKV(context).encode(key, value);
    }

    public static boolean putMMKVString(Context context, String key, String value) {
        if (getMMKV(context) == null)
            return false;
        if (value == null)
            value = "";
        return getMMKV(context).encode(key, value);
    }

    public static boolean putMMKVSetString(Context context, String key, Set<String> set) {
        if (getMMKV(context) == null)
            return false;
        if (set == null)
            return false;
        return getMMKV(context).encode(key, set);
    }

    public static boolean putMMKVFloat(Context context, String key, float value) {
        if (getMMKV(context) == null)
            return false;
        return getMMKV(context).encode(key, value);
    }

    public static boolean putMMKVDouble(Context context, String key, double value) {
        if (getMMKV(context) == null)
            return false;
        return getMMKV(context).encode(key, value);
    }

    public static boolean putMMKVLong(Context context, String key, long value) {
        if (getMMKV(context) == null)
            return false;
        return getMMKV(context).encode(key, value);
    }

    public static boolean putMMKVParcelable(Context context, String key, Parcelable value) {
        if (getMMKV(context) == null)
            return false;
        if (value == null) {
            putMMKVString(context, key, "");
            return true;
        }
        return putMMKVString(context, key, JSON.toJSONString(value));
//        return getMMKV(context).encode(key, value);
    }

    public static void putMMKVList(Context context, String key, List list) {
        putMMKVString(context, key, JSON.toJSONString(list));
    }


    public static void removeMMKVKey(Context context, String key) {
        if (getMMKV(context) == null)
            return;
        getMMKV(context).remove(key);
    }

    /**
     * MMKV方案=============end
     */


}
