package com.baofu.base.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baofu.base.R;


public class EmptyView extends LinearLayout {
    // private ProgressWheel mProgressBar;



    private LinearLayout mErrorLayout;

    private ImageView mErrorImage;

    private TextView mErrorTextView;

    private View mLoadingLayout;


    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void onFinishInflate() {


        super.onFinishInflate();
    }

    private void init(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.view_list_empty,null);
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(view,params);
        mErrorLayout = findViewById(R.id.error_layout);
        mErrorImage=  findViewById(R.id.error_image);
        mErrorTextView=  findViewById(R.id.error_message);
        mLoadingLayout=  findViewById(R.id.loading_layout);
        setVisibility(View.GONE);
    }



    public void showLoading(){
        setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        mErrorImage.setVisibility(View.GONE);
    }

    private void showErrorLayout(){
        setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorImage.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    /**
     * 一张图片,一行文字
     *
     * @param message
     * @param resDrawable
     */
    public void showNothingData( int resDrawable,String message) {

        showErrorLayout();
        if (resDrawable == 0) {
            mErrorImage.setVisibility(View.GONE);
        } else {
            mErrorImage.setImageResource(resDrawable);
        }
        if (TextUtils.isEmpty(message)) {
            mErrorTextView.setVisibility(View.GONE);
        } else {
            mErrorTextView.setText(message);
        }

    }
    public void showNothingDataWithListener( int resDrawable,String message,OnClickListener listener) {

        showErrorLayout();
        if (resDrawable == 0) {
            mErrorImage.setVisibility(View.GONE);
        } else {
            mErrorImage.setImageResource(resDrawable);
        }
        if (TextUtils.isEmpty(message)) {
            mErrorTextView.setVisibility(View.GONE);
        } else {
            mErrorTextView.setText(message);
        }
        mErrorLayout.setOnClickListener(listener);
    }
    public void hide( ) {
        setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
    }


    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }


}
