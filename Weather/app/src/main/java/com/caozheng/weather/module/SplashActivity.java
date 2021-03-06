package com.caozheng.weather.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.caozheng.weather.R;
import com.caozheng.weather.widget.SplashView;

/**
 * @author caozheng
 * Created time on 2017/11/5
 *
 * description: 闪屏页
 */

public class SplashActivity extends AppCompatActivity {

    private static final int DURATION_TIME = 3;
    private static final String IMAGE_URL = "http://ww2.sinaimg.cn/large/72f96cbagw1f5mxjtl6htj20g00sg0vn.jpg";
    private static final String ACTION_URL = "https://github.com/caozhenggit/Weather";

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;

        SplashView.showSplashView(this, DURATION_TIME, R.mipmap.ic_launcher, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onSplashImageClick(String actionUrl) {

            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                finish();
            }
        });

        SplashView.updateSplashData(this, IMAGE_URL, ACTION_URL);
    }
}
