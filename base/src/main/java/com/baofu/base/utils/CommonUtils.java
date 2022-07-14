package com.baofu.base.utils;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;


import com.baofu.base.BaseApplication;

import java.io.File;
import java.util.List;
import java.util.Set;

public class CommonUtils {
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

    public static void sslHandle(final WebView view, final SslErrorHandler handler, SslError error) {


        if (view == null || view.getContext() == null) {
            return;
        }
        try {
            Activity activity = (Activity) view.getContext();
            if (activity.isFinishing()) {
                return;
            }
        } catch (Exception e) {

        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        AlertDialog dialog = null;
        String message = "SSL Certificate error.";
        switch (error.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                message = "The certificate authority is not trusted.";
                break;
            case SslError.SSL_EXPIRED:
                message = "The certificate has expired.";
                break;
            case SslError.SSL_IDMISMATCH:
                message = "The certificate Hostname mismatch.";
                break;
            case SslError.SSL_NOTYETVALID:
                message = "The certificate is not yet valid.";
                break;
        }
        message += " Do you want to continue anyway?";

        builder.setTitle("SSL Certificate Error");
        builder.setMessage(message);
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(view==null||view.getContext()==null){
                    return;
                }
                if (handler != null) {
                    handler.proceed();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(view==null||view.getContext()==null){
                    return;
                }
                if (handler != null) {
                    handler.proceed();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 获取app versionCode
     */
    public static int getAppVersionCode(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取获取app versionName
     */
    public static String getAppVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 是否安装了应用
     * android11需要在Manifest.xml额外添加
     *     <queries>
     *         <package android:name="com.whatsapp" />
     *         <package android:name="com.instagram.android" />
     *     </queries>
     * @param context
     * @param packageName 应用包名
     *
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        boolean hasInstalled = false;
        try {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> list = pm.getInstalledPackages(0);
            for (PackageInfo p : list) {
//            Trace.e("APPUTIL", "-==" + p.packageName);
                if (packageName != null && packageName.equals(p.packageName)) {
                    hasInstalled = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasInstalled;
    }

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if(context==null)
            return false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getApplicationContext().getSystemService(
                            Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                return false;
            }
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取粘贴版数据
     */
    public static String getClipboardContent(Context context) {
        if (context == null)
            return null;
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if (cm == null)
            return null;
        String content = null;
        try {
            //放在onActivityResumed里面调用。 有的手机会出现getPrimaryClip()为null ，此时需要延迟调用。
            ClipData data = cm.getPrimaryClip();
            if (data != null) {
                ClipData.Item item = data.getItemAt(0);
                content = item.getText().toString();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return content;
    }

    /**
     * 设置粘贴板数据
     */
    public static void setClipboardContent(Context context,String text){
        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("main", text);
        cm.setPrimaryClip(clipData);
    }

}
