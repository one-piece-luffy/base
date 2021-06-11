package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.baofu.base.utils.CommonUtils;
import com.baofu.base.utils.MMKVSP;
import com.baofu.base.view.TitleView;

import java.util.HashMap;
import java.util.Map;

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
        UserBean bean=new UserBean();
        bean.name="asdf";
        UserBean.UserFav fav=new UserBean.UserFav();
        fav.fruit="apple";
        bean.fav=fav;
        Map<String,UserBean> map=new HashMap<>();
        map.put("a",bean);
        MMKVSP.putMap(this,"key",map);

        Map<String,UserBean> value=MMKVSP.getMap(this,"key");
        Log.e("asdf",value.get("a").name);
    }
}