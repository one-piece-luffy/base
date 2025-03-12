package com.baofu.base.utils;


import android.util.Base64;

/**
 * 移位加密
 */
public class ShiftUtils {

    /**
     * @param text 移位字符串
     * @param diff 位移
     * @return
     */
    public static String encode(String text, int diff) {
        String base64= base64Encode(text);
        return shift(base64,diff);
    }

    /**
     * decode传入与encode相反的位移
     * @param password 移位字符串
     * @param diff 位移
     * @return
     */
    public static String decode(String password,int diff){

        String decode = shift(password, diff);
        return base64Decode(decode);
    }


    /**
     * 移位操作
     * @param text 移位字符串
     * @param diff 位移
     * @return
     */
    public static String shift(String text, int diff) {
        char[] c=text.toCharArray();
        char[] passWord = new char[c.length];
        for (int i = 0; i < c.length; i++) {
            try {
                passWord[i] = (char) (c[i] + diff);
//                if ((c[i] >= 88 && c[i] <= 90) || c[i] >= 118 && c[i] <= 122)
//                    passWord[i] = (char) (c[i] + 3 - 26);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return new String(passWord);
    }


    /**
     * base64加密
     * @param text
     * @return
     */
    public static String base64Encode(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }
    /**
     * base64解密
     * @param text
     * @return
     */
    public static String base64Decode(String text){
        return new String(Base64.decode(text,Base64.DEFAULT));
    }



}
