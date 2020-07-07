package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.baofu.base.BaseActivity;
import com.baofu.base.utils.AppUtils;
import com.baofu.base.utils.MMKVSP;
import com.baofu.base.view.TitleView;

public class MainActivity extends BaseActivity {
    TitleView mTitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleView = findViewById(R.id.toolbar);
        mTitleView.setTitle(getString(R.string.app_name));
        mTitleView.setRightButton("right");
        mTitleView.setTitleViewListener(new TitleView.BaseTitleViewListener(){
            @Override
            public void onTitleClicked() {
                super.onTitleClicked();
                AppUtils.showToast("title");
            }

            @Override
            public void onTitleMoreClick() {
                super.onTitleMoreClick();
                AppUtils.showToast("more");
            }

            @Override
            public void onRightClicked() {
                AppUtils.showToast("right");
            }
        });
    }
}