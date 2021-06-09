package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.baofu.base.utils.CommonUtils;
import com.baofu.base.view.TitleView;

public class MainActivity extends AppCompatActivity {
    TitleView mTitleView;
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
    }
}