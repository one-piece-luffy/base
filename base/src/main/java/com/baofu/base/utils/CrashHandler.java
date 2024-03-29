package com.baofu.base.utils;

/**
 * Created by yi on 15/10/10.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.baofu.base.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 *  需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler
{

    public static final String TAG = "CrashHandler";

//    public static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/logcat.txt";
    public static final String LOG_FILE_PATH = CommonUtils.getDownloadPath(BaseApplication.getInstance()) + "/logcat.txt";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private  static CrashHandler instance;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String> ();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH-mm-ss");

    private File file ;

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {}

    /** 获取CrashHandler实例 ,单例模式 */
    public  static CrashHandler getInstance() {
        if(instance == null)
            instance = new CrashHandler();
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (Environment.getExternalStorageState ().equals(Environment.MEDIA_MOUNTED)) {
            file= new File(LOG_FILE_PATH);
        }

    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        if(!file.exists()){
            //收集设备参数信息
            collectDeviceInfo(mContext);
        }

        //保存日志文件
        saveCatchInfo2File(ex);

        return true;
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter ();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        Log.e(TAG,result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date ());
            if (Environment.getExternalStorageState ().equals(Environment.MEDIA_MOUNTED)) {
                if(file==null){
                    file= new File(LOG_FILE_PATH);
                }
                //追加写入
                FileOutputStream fos = new FileOutputStream (file,true);
                fos.write("\n\r".getBytes());
                fos.write(time.getBytes());
                fos.write("\n\r".getBytes());
                fos.write(sb.toString().getBytes());

                fos.close();
            }
//            return fileName;
            return null;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

}