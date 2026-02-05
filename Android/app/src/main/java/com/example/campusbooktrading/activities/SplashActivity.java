package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.utils.SessionManager;

/**
 * 启动屏幕 Activity
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 延迟 2 秒后跳转
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(SplashActivity.this);

            Intent intent;
            if (sessionManager.isLoggedIn()) {
                // 已登录，跳转到首页
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                // 未登录，跳转到登录页
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}
