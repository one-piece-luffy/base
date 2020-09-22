package com.baofu.base.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;


import com.baofu.base.BaseApplication;

import java.io.File;
import java.util.List;
import java.util.Set;

public class AppUtils {
    public static boolean isMainProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        if (processInfos == null)
            return true;
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info != null && info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }


    /**
     * Toast提示
     *
     * @param message
     */
    public static void showToast(Object message) {
        BaseApplication.getInstance().showToast(message, Toast.LENGTH_SHORT, true);
    }
    /**
     * Toast提示
     *
     * @param message
     */
    public static void showToast(Context context,Object message) {
        BaseApplication.getInstance().showToast(context,message, Toast.LENGTH_SHORT, true);
    }

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

    public static String getDownloadPath(Context context) {

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return context.getFilesDir().getAbsolutePath();
        File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (file != null)
            return file.getAbsolutePath();
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (file != null)
            return file.getAbsolutePath();
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 检测辅助功能是否开启
     * @param context
     * @return
     */
    public static boolean enabledAccessibility(Context context) {
        int accessibilityEnabled = 0;
        final String service = "com.ysit.wonderfulmoment.monitor_notify.service.AppAccessibilityService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
        }

        return accessibilityFound;

    }

    //检测通知监听服务是否被授权
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;

//        String pkgName = context.getPackageName();
//        final String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
//        if (!TextUtils.isEmpty(flat)) {
//            final String[] names = flat.split(":");
//            for (int i = 0; i < names.length; i++) {
//                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;

    }


}
