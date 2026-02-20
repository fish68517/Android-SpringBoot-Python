package com.example.campusbooktrading.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;
import com.example.campusbooktrading.utils.SessionStateManager;

/**
 * 基础 Activity 类 - 提供通用的错误处理、加载状态管理和会话管理
 */
public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog progressDialog;
    protected SessionManager sessionManager;
    protected SessionStateManager sessionStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 初始化会话管理器
        sessionManager = new SessionManager(this);
        sessionStateManager = SessionStateManager.getInstance();
        
        // 初始化会话状态管理器（如果还未初始化）
        if (sessionStateManager != null) {
            sessionStateManager.initialize(sessionManager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // 在屏幕转换时检查会话状态
      /*  if (sessionStateManager != null) {
            sessionStateManager.checkSessionOnActivityResume(this);
        }*/
    }

    /**
     * 显示加载对话框
     */
    protected void showLoadingDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    protected void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 显示进度条
     */
    protected void showProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏进度条
     */
    protected void hideProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 检查网络连接
     */
    protected boolean checkNetworkConnection() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, null);
            return false;
        }
        return true;
    }

    /**
     * 显示错误消息
     */
    protected void showError(String message) {
        ErrorHandler.showSnackbar(this, message);
    }

    /**
     * 显示成功消息
     */
    protected void showSuccess(String message) {
        ErrorHandler.showSnackbar(this, message);
    }

    /**
     * 显示信息消息
     */
    protected void showInfo(String message) {
        ErrorHandler.showSnackbar(this, message);
    }

    /**
     * 显示错误对话框
     */
    protected void showErrorDialog(String title, String message) {
        ErrorHandler.showErrorDialog(this, title, message);
    }

    /**
     * 显示重试对话框
     */
    protected void showRetryDialog(String message, Runnable onRetry) {
        ErrorHandler.showRetryDialog(this, message, onRetry);
    }

    /**
     * 刷新会话（延长会话时间）
     */
    protected void refreshSession() {
        if (sessionStateManager != null) {
            sessionStateManager.refreshSession();
        }
    }

    /**
     * 执行登出
     */
    protected void performLogout() {
        if (sessionStateManager != null) {
            sessionStateManager.logout();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
    }
}
