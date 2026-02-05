package com.example.campusbooktrading;

import android.app.Application;

import com.example.campusbooktrading.api.ApiClient;

/**
 * 应用程序主类 - 初始化全局资源
 */
public class CampusBookTradingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        // 初始化 API 客户端
        ApiClient.initialize(this);
    }
}
