package com.example.baseproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.baofu.base.view.EmptyView;

public class MainActivity extends AppCompatActivity {
    EmptyView emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emptyView=findViewById(R.id.emptyview);
        emptyView.showLoading();
        emptyView.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyView.showNothingData(0,"no data");
            }
        },1000);
        emptyView.postDelayed(new Runnable() {
            @Override
            public void run() {
                emptyView.hide();
            }
        },3000);
    }
}