
package com.baofu.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baofu.base.utils.CommonUtils;


/**
 * Created by lihaiyi on 2018/7/6.
 */

public class BaseTitleView extends FrameLayout {

    public static final int DEFAULT_PADDING_LFET_RIGHT = 15;
    public static final int DEFAULT_PADDING_TOP_BOTTOM = 5;

    public static final int LEFT_TEXT_SIZE_DP = 16;

    public static final int CENTER_TEXT_SIZE_DP = 20;

    public static final int TEXT_COLOR = 0xffFFFAFA;

    private static final int DEFALUT_MAX_WIDTH_DP = 120;

    int topMargin;

    // mCenterLayout的左右两边默认间距
    int defaultCenterMargin;

    // 左边布局
    LinearLayout mLeftLayout;

    // 右边布局
    LinearLayout mRightLayout;

    // 中间布局
    LinearLayout mCenterLayout;

    // 底部阴影
    View mBottomLine;

    TitleViewListener mTitleViewListener;

    LayoutInflater layoutInflater;

    /**
     * custom start===========
     */
    ImageView mTitleBackgroundView;


    //点击5次
    private final int CLICK_NUM = 5;
    //点击时间间隔5秒
    private final int CLICK_INTERVER_TIME = 3000;
    //上一次的点击时间
    private long lastClickTime = 0;
    //记录点击次数
    private int clickNum = 0;


    OnClickListener titleOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null){
                mTitleViewListener.onTitleClicked();
                moreClick();
            }
        }
    };

    OnClickListener leftOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null){
                mTitleViewListener.onLeftClicked();
                moreClick();
            }
        }
    };

    OnClickListener rightOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null)
                mTitleViewListener.onRightClicked();
        }
    };

    public BaseTitleView(Context context) {
        super(context);
        initView();
    }

    public BaseTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BaseTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        layoutInflater = LayoutInflater.from(getContext());

    }

    private void initView() {
        topMargin = getStatusBarHeight(getContext());
        defaultCenterMargin = dip2px(getContext(), 60);

        initBackGroundImageView();
        initBottomLine();
        initLeftLayout();
        initRightLayout();
        initCenterLayout();
//        setPaddingTop(AppUtils.getStatusBarHeight(getContext()));
        refreshCenterView();
    }

    public View getBottomLine() {
        return mBottomLine;
    }

    public void setBottomLine(View bottomLine) {
        if (mBottomLine != null) {
            removeView(mBottomLine);
        }
        this.mBottomLine = bottomLine;
        mBottomLine.setVisibility(View.GONE);
        addView(mBottomLine);
    }

    public ImageView getTitleBackgroundView() {
        return mTitleBackgroundView;
    }

    public void setTitleBackgroundView(ImageView titleBackgroundView) {
        if (mTitleBackgroundView != null) {
            removeView(mTitleBackgroundView);
        }
        this.mTitleBackgroundView = titleBackgroundView;
        addView(mTitleBackgroundView, 0);
    }

    public LinearLayout getLeftLayout() {
        return mLeftLayout;
    }

    public LinearLayout getRightLayout() {
        return mRightLayout;
    }

    public LinearLayout getCenterLayout() {
        return mCenterLayout;
    }

    /**
     * titlebar的背景
     */
    private void initBackGroundImageView() {
        mTitleBackgroundView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mTitleBackgroundView.setLayoutParams(layoutParams);
        addView(mTitleBackgroundView, 0);
    }

    /**
     * 底部阴影
     */
    private void initBottomLine() {
        mBottomLine = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, CommonUtils.dip2px(getContext(), 0.5f));
        layoutParams.gravity = Gravity.BOTTOM;
        mBottomLine.setLayoutParams(layoutParams);
        mBottomLine.setBackgroundColor(0xffeff1f4);
        mBottomLine.setVisibility(View.GONE);
        addView(mBottomLine);
    }

    /**
     * titleBar左边的view
     */
    private void initLeftLayout() {
        mLeftLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.START;
        mLeftLayout.setLayoutParams(layoutParams);
        addView(mLeftLayout);
    }

    /**
     * titleBar右边的view
     */
    private void initRightLayout() {
        mRightLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.END;
        mRightLayout.setLayoutParams(layoutParams);
        addView(mRightLayout);
    }

    /**
     * titleBar中间的view
     */
    private void initCenterLayout() {
        mCenterLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        mCenterLayout.setLayoutParams(layoutParams);
        addView(mCenterLayout);
    }

    /**
     * 左边为图片样式
     * 
     * @param resDrawableId
     */
    public void setLeftView(int resDrawableId) {
        mLeftLayout.removeAllViews();
        if (resDrawableId != 0) {
            ImageView imageView = new ImageView(getContext());
            setLeftViewAttr(imageView, true);

            imageView.setImageResource(resDrawableId);
            imageView.setOnClickListener(leftOnClickListener);
            mLeftLayout.addView(imageView);
        }
    }

    /**
     * 左边为文字的样式
     * 
     * @param text
     */
    public void setLeftView(String text) {
        mLeftLayout.removeAllViews();
        if (!TextUtils.isEmpty(text)) {
            Button btn = new Button(getContext());
            btn.setMaxWidth(CommonUtils.dip2px(getContext(),DEFALUT_MAX_WIDTH_DP));
            setLeftViewAttr(btn, true);
            btn.setText(text);
            btn.setMaxLines(1);
            btn.setEllipsize(TextUtils.TruncateAt.END);
            btn.setTextColor(TEXT_COLOR);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LEFT_TEXT_SIZE_DP);
            btn.setOnClickListener(leftOnClickListener);
            mLeftLayout.addView(btn);
        }
    }

    /**
     * 左边自定义的view
     * 
     * @param view
     */
    public void setLeftView(View view) {
        setLeftView(view, false);
    }

    /**
     * 左边自定义的view
     * 
     * @param view
     * @param withDefaultStyle 使用默认的一些属性(padding,background,gravity)
     */
    public void setLeftView(View view, boolean withDefaultStyle) {
        mLeftLayout.removeAllViews();
        if (view != null) {
            if (withDefaultStyle) {
                setLeftViewAttr(view, true);
            }
            mLeftLayout.addView(view);
        }
    }

    /**
     * 右边为图片样式
     * 
     * @param resDrawableId
     */
    public void setRightView(int resDrawableId) {
        mRightLayout.removeAllViews();
        if (resDrawableId != 0) {
            ImageView imageView = new ImageView(getContext());
            setLeftViewAttr(imageView, true);

            imageView.setImageResource(resDrawableId);
            imageView.setOnClickListener(rightOnClickListener);
            mRightLayout.addView(imageView);
        }
    }

    /**
     * 右边为文字的样式
     * 
     * @param text
     */
    public void setRightView(String text) {
        mRightLayout.removeAllViews();
        if (!TextUtils.isEmpty(text)) {
            Button btn = new Button(getContext());
            btn.setMaxWidth(CommonUtils.dip2px(getContext(),DEFALUT_MAX_WIDTH_DP));
            setLeftViewAttr(btn, true);
            btn.setText(text);
            btn.setMaxLines(1);
            btn.setEllipsize(TextUtils.TruncateAt.END);
            btn.setTextColor(TEXT_COLOR);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, LEFT_TEXT_SIZE_DP);
            btn.setOnClickListener(rightOnClickListener);
            mRightLayout.addView(btn);
        }
    }

    /**
     * 右边自定义的view
     * 
     * @param view
     */
    public void setRightView(View view) {
        setRightView(view, true);
    }

    /**
     * 右边自定义的view
     * 
     * @param view
     * @param withDefaultStyle 使用默认的一些属性(padding,background,gravity)
     */
    public void setRightView(View view, boolean withDefaultStyle) {
        mRightLayout.removeAllViews();
        if (view != null) {
            if (withDefaultStyle) {
                setLeftViewAttr(view, true);
            }
            mRightLayout.addView(view);
        }
    }

    // todo

    /**
     * 左边为图片样式
     * 
     * @param resDrawableId
     */
    public void setCenterView(int resDrawableId) {
        mCenterLayout.removeAllViews();
        if (resDrawableId != 0) {
            ImageView imageView = new ImageView(getContext());
            setLeftViewAttr(imageView, false);
            imageView.setImageResource(resDrawableId);
            imageView.setOnClickListener(titleOnClickListener);
            mCenterLayout.addView(imageView);
        }
    }
    

    /**
     * 中间为文字的样式
     * 
     * @param text
     */
    public void setCenterView(String text) {
        mCenterLayout.removeAllViews();
        if (!TextUtils.isEmpty(text)) {
            Button btn = new Button(getContext());
            setLeftViewAttr(btn, false);
            btn.setText(text);
            btn.setMaxLines(1);
            btn.setAllCaps(false);
            btn.setEllipsize(TextUtils.TruncateAt.END);
            btn.setTextColor(TEXT_COLOR);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, CENTER_TEXT_SIZE_DP);
            btn.setOnClickListener(titleOnClickListener);
            mCenterLayout.addView(btn);
        }
    }

   

    /**
     * 中间为文字的样式
     * 
     * @param text
     * @param textSize 字号dp
     */
    public void setCenterView(String text, float textSize, String textColor) {
        mCenterLayout.removeAllViews();
        if (!TextUtils.isEmpty(text)) {
            Button btn = new Button(getContext());
            setLeftViewAttr(btn, false);
            btn.setText(text);
            btn.setText(text);
            btn.setMaxLines(1);
            btn.setEllipsize(TextUtils.TruncateAt.END);
            if (TextUtils.isEmpty(textColor)) {
                btn.setTextColor(TEXT_COLOR);
            } else {
                try {
                    btn.setTextColor(Color.parseColor(textColor));
                } catch (Exception e) {
                    btn.setTextColor(TEXT_COLOR);
                }
            }
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    textSize > 0 ? textSize : CENTER_TEXT_SIZE_DP);

            btn.setOnClickListener(titleOnClickListener);
            mCenterLayout.addView(btn);
        }
    }

    /**
     * 中间自定义的view
     * 
     * @param view
     */
    public void setCenterView(View view) {
        setCenterView(view, false);
    }

    /**
     * 中间自定义的view
     * 
     * @param view
     * @param withDefaultStyle 使用默认的一些属性(padding,background,gravity)
     */
    public void setCenterView(View view, boolean withDefaultStyle) {
        mCenterLayout.removeAllViews();
        if (view != null) {
            if (withDefaultStyle) {
                setLeftViewAttr(view, false);
            }
            mCenterLayout.addView(view);
        }
    }

    /**
     * centerView居中
     */
    void refreshCenterView() {
        post(new Runnable() {
            @Override
            public void run() {
                int maxWidth = mLeftLayout == null ? 0 : mLeftLayout.getWidth();
                int rightWidth = mRightLayout == null ? 0 : mRightLayout.getWidth();
                if (rightWidth > maxWidth) {
                    maxWidth = rightWidth;
                }
                if (maxWidth < defaultCenterMargin) {
                    maxWidth = defaultCenterMargin;
                }
                LayoutParams params = (LayoutParams)mCenterLayout.getLayoutParams();
                params.setMargins(maxWidth, params.topMargin, maxWidth, params.bottomMargin);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(maxWidth);
                    params.setMarginEnd(maxWidth);
                }
                mCenterLayout.setLayoutParams(params);
//                Trace.e("titleview","-==height:"+getHeight());

            }
        });
    }

    private void setLeftViewAttr(View view, boolean needPressStatus) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;

        view.setLayoutParams(layoutParams);
        view.setPadding(dip2px(getContext(), DEFAULT_PADDING_LFET_RIGHT), dip2px(getContext(), DEFAULT_PADDING_TOP_BOTTOM),
                dip2px(getContext(), DEFAULT_PADDING_LFET_RIGHT), dip2px(getContext(), DEFAULT_PADDING_TOP_BOTTOM));


        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable normal = new GradientDrawable();
        normal.setColor(0x00000000);

        if (needPressStatus) {
            GradientDrawable press = new GradientDrawable();
            press.setColor(0x33000000);
            stateListDrawable.addState(new int[] {
                    android.R.attr.state_pressed
            }, press);
        }

        stateListDrawable.addState(new int[] {}, normal);

        view.setBackground(stateListDrawable);

    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    private int getStatusBarHeight(Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height;
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        else
            height = (int)Math.ceil(25 * resources.getDisplayMetrics().density);
        return height;
    }

    public void setTitleViewListener(TitleViewListener listener) {
        mTitleViewListener = listener;
    }

    private void setPaddingTop(final int paddingTop) {
        post(new Runnable() {
            @Override
            public void run() {
                if (mLeftLayout != null) {

                    mLeftLayout.setPadding(0, paddingTop, 0, 0);
                }
                if (mRightLayout != null) {

                    mRightLayout.setPadding(0, paddingTop, 0, 0);
                }
                if (mCenterLayout != null) {

                    mCenterLayout.setPadding(0, paddingTop, 0, 0);
                }
            }
        });

    }

    /**
     * 点击N次启动debug界面
     */
    public void moreClick() {
        //点击的间隔时间不能超过5秒
        long currentClickTime = SystemClock.uptimeMillis();
        if (currentClickTime - lastClickTime <= CLICK_INTERVER_TIME || lastClickTime == 0) {
            lastClickTime = currentClickTime;
            clickNum = clickNum + 1;
        } else {
            //超过3秒的间隔
            //重新计数 从1开始
            clickNum = 1;
            lastClickTime = 0;
            return;
        }
        if (clickNum == CLICK_NUM) {
            //重新计数
            clickNum = 0;
            lastClickTime = 0;
            /*实现点击多次后的事件*/
            if(mTitleViewListener!=null){
                mTitleViewListener.onTitleMoreClick();
            }
        }
    }

    public interface TitleViewListener {
        void onLeftClicked();

        void onLeft1Clicked();

        void onRightClicked();

        void onRight1Clicked();

        void onTitleClicked();

        void onTitleMoreClick();
    }

}
