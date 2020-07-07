package com.baofu.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 将时间戳转换为时间
     * @param time 时间戳
     * @param formatString 格式化样式 "yyyy-MM-dd HH:mm"
     * @return
     */
    public static String stampToDate(long time,String formatString){
        SimpleDateFormat sdf=new SimpleDateFormat(formatString);
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(time))));
        return sd;
    }
}
