package com.baofu.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.baofu.base.utils.CommonUtils;
import com.baofu.base.utils.CrashHandler;
import com.tencent.mmkv.MMKV;


public class BaseApplication extends MultiDexApplication {

    private static BaseApplication mInstance;
    private  Toast mToast;
    private View mView;

    public static BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        if(CommonUtils.isMainProcess(this)){
//            MMKV.initialize(this);
//        }

    }
    public void showToast(Context context,Object message, int duration, boolean useSystemView) {
        if (message == null || TextUtils.isEmpty(message.toString())) {
            return;
        }
        if(useSystemView){
            // 用成员变量保存引用,避免多次点击会叠加Toast问题
            if (mToast == null) {
                mToast = new Toast(context);
                if (mView == null) {
                    mView = Toast.makeText(this, "", Toast.LENGTH_SHORT).getView();
                }
                mToast.setView(mView);
            }
            //todo 临时处理================
            mToast.cancel();
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            //临时处理end================


            mToast.setDuration(duration);
            mToast.setText(message.toString());

            mToast.show();
        }
        else {
            // 用成员变量保存引用,避免多次点击会叠加Toast问题
            if (mToast == null) {
                mToast = new Toast(context);
                mToast.setView(LayoutInflater.from(context).inflate(R.layout.view_toast, null));
            }

            //todo 临时处理================
            mToast.cancel();
            mToast = new Toast(context);
            mToast.setView(LayoutInflater.from(context).inflate(R.layout.view_toast, null));
            //临时处理end================


            ((TextView) mToast.getView().findViewById(R.id.toast_text)).setText(message.toString());

            mToast.setDuration(duration);

            mToast.show();
        }


    }

    /**
     *
     * @param message
     * @param duration
     * @param useSystemView 是否使用系統自定義的佈局
     */
    public void showToast(Object message, int duration,boolean useSystemView) {
        showToast(getApplicationContext(),message,duration,useSystemView);


    }
}
