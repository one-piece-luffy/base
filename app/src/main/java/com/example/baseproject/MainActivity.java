package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.baofu.base.utils.CommonUtils;
import com.baofu.base.utils.MMKVSP;
import com.baofu.base.view.EmptyView;
import com.baofu.base.view.TitleView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TitleView mTitleView;
    EmptyView emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitleView = findViewById(R.id.toolbar);
        mTitleView.setBackgroundColor(0xffff0000);
        mTitleView.setTitle(getString(R.string.app_name));
        mTitleView.setRightButton("right");
        mTitleView.setTitleViewListener(new TitleView.BaseTitleViewListener(){
            @Override
            public void onTitleClicked() {
                super.onTitleClicked();
                CommonUtils.showToast("title");
            }

            @Override
            public void onTitleMoreClick() {
                super.onTitleMoreClick();
                CommonUtils.showToast("more");
            }

            @Override
            public void onRightClicked() {
                CommonUtils.showToast("right");
            }
        });

        emptyView=findViewById(R.id.emptyview);
        emptyView.showLoading();
        emptyView.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyView.hide();
            }
        },1000);
    }
}