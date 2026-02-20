package com.example.campusbooktrading.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 会话管理器 - 管理用户登录状态、令牌和会话过期
 */
public class SessionManager {
    private static final String PREF_NAME = "CampusBookTrading";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STUDENT_ID = "student_id";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_TOKEN_TIMESTAMP = "token_timestamp";
    private static final String KEY_SESSION_TIMEOUT = "session_timeout";
    
    // 会话超时时间：24小时（毫秒）
    private static final long SESSION_TIMEOUT_MS = 24 * 60 * 60 * 1000;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 保存登录信息
     */
    public void saveLoginInfo(int userId, String username, String email, String studentId, String token) {
        long currentTime = System.currentTimeMillis();
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_STUDENT_ID, studentId);
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(KEY_TOKEN_TIMESTAMP, currentTime);
        editor.putLong(KEY_SESSION_TIMEOUT, SESSION_TIMEOUT_MS);
        editor.apply();
    }

    /**
     * 获取用户 ID
     */
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    /**
     * 获取用户名
     */
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    /**
     * 获取邮箱
     */
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    /**
     * 获取学号
     */
    public String getStudentId() {
        return sharedPreferences.getString(KEY_STUDENT_ID, "");
    }

    /**
     * 获取令牌
     */
    public String getToken() {
        if (isTokenExpired()) {
            logout();
            return "";
        }
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    /**
     * 获取授权头
     */
    public String getAuthorizationHeader() {
        String token = getToken();
        return token.isEmpty() ? "" : "Bearer " + token;
    }

    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        if (!sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            return false;
        }
        
        // 检查令牌是否过期
        if (isTokenExpired()) {
            logout();
            return false;
        }
        
        return true;
    }

    /**
     * 检查令牌是否过期
     */
    public boolean isTokenExpired() {
        long tokenTimestamp = sharedPreferences.getLong(KEY_TOKEN_TIMESTAMP, 0);
        long sessionTimeout = sharedPreferences.getLong(KEY_SESSION_TIMEOUT, SESSION_TIMEOUT_MS);
        long currentTime = System.currentTimeMillis();
        
        return (currentTime - tokenTimestamp) > sessionTimeout;
    }

    /**
     * 获取令牌剩余有效时间（毫秒）
     */
    public long getTokenRemainingTime() {
        long tokenTimestamp = sharedPreferences.getLong(KEY_TOKEN_TIMESTAMP, 0);
        long sessionTimeout = sharedPreferences.getLong(KEY_SESSION_TIMEOUT, SESSION_TIMEOUT_MS);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - tokenTimestamp;
        
        long remainingTime = sessionTimeout - elapsedTime;
        return Math.max(0, remainingTime);
    }

    /**
     * 刷新令牌时间戳（延长会话）
     */
    public void refreshTokenTimestamp() {
        editor.putLong(KEY_TOKEN_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * 更新令牌
     */
    public void updateToken(String newToken) {
        editor.putString(KEY_TOKEN, newToken);
        editor.putLong(KEY_TOKEN_TIMESTAMP, System.currentTimeMillis());
        editor.apply();
    }

    /**
     * 清除登录信息（登出）
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }

    /**
     * 获取会话超时时间（毫秒）
     */
    public long getSessionTimeout() {
        return sharedPreferences.getLong(KEY_SESSION_TIMEOUT, SESSION_TIMEOUT_MS);
    }

    /**
     * 设置会话超时时间（毫秒）
     */
    public void setSessionTimeout(long timeoutMs) {
        editor.putLong(KEY_SESSION_TIMEOUT, timeoutMs);
        editor.apply();
    }

    public void clearLoginInfo() {

    }
}
