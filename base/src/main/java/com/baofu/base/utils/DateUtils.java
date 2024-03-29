package com.baofu.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    /**
     * 将时间戳转换为时间
     * @param time 时间戳 单位毫秒
     * @param formatString 格式化样式 "yyyy-MM-dd HH:mm"
     * @return
     */
    public static String stampToDate(long time,String formatString){
        SimpleDateFormat sdf=new SimpleDateFormat(formatString);
        String sd = sdf.format(new Date(time));
        return sd;
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }


    /**

     * 计算两个日期之间相差的天数

     * @param smdate 较小的时间

     * @param bdate 较大的时间

     * @return 相差天数

     * @throws ParseException

     */

    public static int daysBetweenByMillisecond(Date smdate,Date bdate) throws ParseException {

        Calendar cal = Calendar.getInstance();

        cal.setTime(smdate);

        long time1 = cal.getTimeInMillis();

        cal.setTime(bdate);

        long time2 = cal.getTimeInMillis();

        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));

    }

    /**
     *
     * 计算两个日期之间相差的天数
     * @param date1 较小的时间
     * @param date2 较大的时间
     * @return date2-date1
     */
    public static int daysBetween(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)    //不同年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else
        {//同一年
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }

    public static String formatTime( String time, String formatString) {
        if (time == null || "".equals(time)) {
            return "";
        }
        String str = "";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        try {

            Calendar cal = Calendar.getInstance();
            int localDay = cal.get(Calendar.DAY_OF_YEAR);

            Date d = format.parse(time);
            format = new SimpleDateFormat(formatString);

            // format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            format.setTimeZone(Calendar.getInstance().getTimeZone());
//            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            str = format.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
