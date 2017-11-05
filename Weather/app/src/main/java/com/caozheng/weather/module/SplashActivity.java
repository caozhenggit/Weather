package com.caozheng.weather.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.caozheng.weather.MainActivity;
import com.caozheng.weather.R;
import com.caozheng.weather.widget.SplashView;

/**
 * @author caozheng
 * Created time on 2017/11/5
 *
 * description: 闪屏页
 */

public class SplashActivity extends AppCompatActivity{

    private int durationTime = 6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashView.showSplashView(this, durationTime, R.mipmap.ic_launcher, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onSplashImageClick(String actionUrl) {

            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                if(initiativeDismiss){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));

                    finish();
                }
            }
        });

        SplashView.updateSplashData(this, "http://ww2.sinaimg.cn/large/72f96cbagw1f5mxjtl6htj20g00sg0vn.jpg", "http://jkyeo.com");
    }
}
