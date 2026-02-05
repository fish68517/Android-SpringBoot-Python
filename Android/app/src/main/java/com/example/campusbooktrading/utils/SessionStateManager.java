package com.example.campusbooktrading.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.campusbooktrading.activities.LoginActivity;

/**
 * 会话状态管理器 - 管理应用级别的会话状态和屏幕转换
 */
public class SessionStateManager {
    private static SessionStateManager instance;
    private SessionManager sessionManager;
    private Activity currentActivity;
    private boolean isSessionValid = true;

    private SessionStateManager() {
    }

    /**
     * 获取单例实例
     */
    public static synchronized SessionStateManager getInstance() {
        if (instance == null) {
            instance = new SessionStateManager();
        }
        return instance;
    }

    /**
     * 初始化会话状态管理器
     */
    public void initialize(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 设置当前活动
     */
    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    /**
     * 检查会话是否有效
     */
    public boolean isSessionValid() {
        if (sessionManager == null) {
            return false;
        }
        
        // 检查用户是否已登录
        if (!sessionManager.isLoggedIn()) {
            isSessionValid = false;
            return false;
        }

        // 检查令牌是否过期
        if (sessionManager.isTokenExpired()) {
            isSessionValid = false;
            performSessionExpired();
            return false;
        }

        isSessionValid = true;
        return true;
    }

    /**
     * 在屏幕转换时检查会话
     */
    public void checkSessionOnActivityResume(Activity activity) {
        setCurrentActivity(activity);
        
        if (!isSessionValid()) {
            // 会话无效，跳转到登录页面
            redirectToLogin();
        }
    }

    /**
     * 刷新会话（延长会话时间）
     */
    public void refreshSession() {
        if (sessionManager != null && sessionManager.isLoggedIn()) {
            sessionManager.refreshTokenTimestamp();
            isSessionValid = true;
        }
    }

    /**
     * 执行会话过期处理
     */
    private void performSessionExpired() {
        if (sessionManager != null) {
            sessionManager.logout();
        }
        redirectToLogin();
    }

    /**
     * 重定向到登录页面
     */
    private void redirectToLogin() {
        if (currentActivity != null) {
            Intent intent = new Intent(currentActivity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            currentActivity.startActivity(intent);
            currentActivity.finish();
        }
    }

    /**
     * 执行登出
     */
    public void logout() {
        if (sessionManager != null) {
            sessionManager.logout();
        }
        isSessionValid = false;
        redirectToLogin();
    }

    /**
     * 获取会话剩余时间（毫秒）
     */
    public long getSessionRemainingTime() {
        if (sessionManager != null) {
            return sessionManager.getTokenRemainingTime();
        }
        return 0;
    }

    /**
     * 获取会话超时时间（毫秒）
     */
    public long getSessionTimeout() {
        if (sessionManager != null) {
            return sessionManager.getSessionTimeout();
        }
        return 0;
    }
}
