
package com.baofu.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class TitleView extends BaseTitleView {

    public TitleView(Context context) {
        super(context);
        init();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    OnClickListener left1OnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null)
                mTitleViewListener.onLeft1Clicked();
        }
    };

    OnClickListener right1OnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null)
                mTitleViewListener.onRight1Clicked();
        }
    };

    public static class BaseTitleViewListener implements TitleView.TitleViewListener {

        @Override
        public void onLeftClicked() {

        }

        @Override
        public void onLeft1Clicked() {

        }

        @Override
        public void onRightClicked() {

        }

        @Override
        public void onRight1Clicked() {

        }

        @Override
        public void onTitleClicked() {

        }

        @Override
        public void onTitleMoreClick() {

        }

    }

    private void init() {
        // TODO: 2020/5/7
//        setCenterView(R.drawable.dongqiudi_name);
    }

    public void setLeftButton(int res) {
        setLeftView(res);
    }

    public void setLeftButton(String text) {
        setLeftView(text);
    }

    public void setRightButton(int res) {
        setRightView(res);
    }

    public void setRightButton(String text) {
        setRightView(text);
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            // TODO: 2020/5/7
//            setCenterView(R.drawable.dongqiudi_name);
        } else {

            setCenterView(title);
        }
    }

    public void setTitle(String title, int textSize) {
        setCenterView(title, textSize, null);
    }

    public void setTitleWithColor(String title, String color) {
        setCenterView(title, -1, color);
    }

    public void hideTitle() {
        setCenterView("");
    }


    public void setRightButton(boolean selected) {

//        ImageView btn = (ImageView)this.layoutInflater
//                .inflate(com.allfootball.news.businessbase.R.layout.view_titlebar_right_btn, null);
//        btn.setOnClickListener(this.rightOnClickListener);
//        btn.setSelected(selected);
//        setRightView(btn);
    }

}
