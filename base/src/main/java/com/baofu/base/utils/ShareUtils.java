package com.baofu.base.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class ShareUtils {


    public static void shareSystemFile(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            AppUtils.showToast("file dont exists");
            return;
        }

        try {


            ComponentName comp = new ComponentName("com.tencent.mobileqq",
                    "com.tencent.mobileqq.activity.JumpActivity");
            Intent shareIntent = new Intent();
            Uri audioUri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 添加这一句表示对目标应用临时授权该Uri所代表的文件
                audioUri = FileProvider.getUriForFile(context,
                        "com.ysit.new_monitor" + ".fileProvider", file);// 通过FileProvider创建一个content类型的Uri
            } else {
                audioUri = Uri.fromFile(file);
            }
//        shareIntent.setComponent(comp);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, audioUri);
            shareIntent.setType("*/*");
            context.startActivity(Intent.createChooser(shareIntent, "file"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
