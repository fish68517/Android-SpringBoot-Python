package com.example.campusbooktrading.utils;

import android.content.Context;
import android.content.Intent;

import com.example.campusbooktrading.activities.LoginActivity;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 令牌拦截器 - 处理令牌过期和自动登出
 */
public class TokenInterceptor implements Interceptor {
    private Context context;
    private SessionManager sessionManager;

    public TokenInterceptor(Context context) {
        this.context = context;
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // 检查令牌是否过期
        if (sessionManager.isTokenExpired()) {
            // 令牌已过期，执行自动登出
            performAutoLogout();
            // 返回 401 错误
            return chain.proceed(originalRequest);
        }

        // 添加授权头
        String authHeader = sessionManager.getAuthorizationHeader();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        
        if (!authHeader.isEmpty()) {
            requestBuilder.header("Authorization", authHeader);
        }

        Request newRequest = requestBuilder.build();
        Response response = chain.proceed(newRequest);

        // 检查响应状态码
        if (response.code() == 401) {
            // 令牌无效或过期，执行自动登出
            performAutoLogout();
        }

        return response;
    }

    /**
     * 执行自动登出
     */
    private void performAutoLogout() {
        // 清除会话
        sessionManager.logout();

        // 跳转到登录页面
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
