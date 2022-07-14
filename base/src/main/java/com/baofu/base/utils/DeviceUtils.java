package com.baofu.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.baofu.base.BaseApplication;

import java.util.Locale;

public class DeviceUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null)
            context = BaseApplication.getInstance();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height;
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        else
            height = (int) Math.ceil(25 * resources.getDisplayMetrics().density);
        return height;
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }
    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }


    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().toString();
    }


    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机设备名
     *
     * @return  手机设备名
     */
    public static String getSystemDevice() {
        return Build.DEVICE;
    }
    @SuppressLint("HardwareIds")
    public static String getUuid(Context context) {
//        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        if (TextUtils.isEmpty(id)) {
//            id = Build.SERIAL;
//        }
//        return id;
        String uuid = CommonSharePreference.getUUID(context);
        if (TextUtils.isEmpty(uuid)) {
            try {
                uuid = java.util.UUID.randomUUID().toString();
                CommonSharePreference.saveUUID(context, uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uuid;
    }

    public static String getDeviceInfo(Context context) {

        return "语言:" + getSystemLanguage() + " 版本号：" + getSystemVersion() + " 型号:" + getSystemModel() + " 名称：" + getSystemDevice() + "uuid:" + getUuid(context);
    }
}
