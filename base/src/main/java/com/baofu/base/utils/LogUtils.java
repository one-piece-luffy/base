package com.baofu.base.utils;

import android.util.Log;


import com.baofu.base.BaseApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogUtils {
//    public static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/notify";
    public static final String LOG_FILE_PATH =  CommonUtils.getDownloadPath(BaseApplication.getInstance()) + "/log";
    public static File fileNew = new File(LOG_FILE_PATH);
    public static void saveLog(String str){
        Log.i("LogUtils",str);
        try {
            FileWriter fileWriter = new FileWriter(fileNew, true);
            fileWriter.write(DateUtils.stampToDate(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")+">>>"+str+"\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
