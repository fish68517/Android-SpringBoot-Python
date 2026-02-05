package com.example.campusbooktrading.utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Response;

/**
 * 错误处理工具类 - 统一处理 API 错误和用户反馈
 */
public class ErrorHandler {

    /**
     * 显示 Snackbar 消息
     */
    public static void showSnackbar(Activity activity, String message) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            Snackbar.make(activity.getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(activity.getResources().getColor(android.R.color.darker_gray))
                    .show();
        }
    }

    /**
     * 显示 Snackbar 消息（带自定义时长）
     */
    public static void showSnackbar(Activity activity, String message, int duration) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            Snackbar.make(activity.getWindow().getDecorView().getRootView(), message, duration)
                    .setBackgroundTint(activity.getResources().getColor(android.R.color.darker_gray))
                    .show();
        }
    }

    /**
     * 显示 Snackbar 消息（带操作按钮）
     */
    public static void showSnackbarWithAction(Activity activity, String message, String actionText, 
                                              Runnable action) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            Snackbar.make(activity.getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(activity.getResources().getColor(android.R.color.darker_gray))
                    .setAction(actionText, v -> action.run())
                    .show();
        }
    }

    /**
     * 显示错误对话框（Material Design 3）
     */
    public static void showErrorDialog(Context context, String title, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * 显示错误对话框（带回调，Material Design 3）
     */
    public static void showErrorDialog(Context context, String title, String message, 
                                       Runnable onDismiss) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    if (onDismiss != null) {
                        onDismiss.run();
                    }
                })
                .show();
    }

    /**
     * 显示重试对话框（Material Design 3）
     */
    public static void showRetryDialog(Context context, String message, Runnable onRetry) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("操作失败")
                .setMessage(message)
                .setPositiveButton("重试", (dialog, which) -> {
                    dialog.dismiss();
                    if (onRetry != null) {
                        onRetry.run();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * 显示网络连接错误对话框（Material Design 3）
     */
    public static void showNetworkErrorDialog(Context context, Runnable onRetry) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("网络连接错误")
                .setMessage("无法连接到网络。请检查您的网络连接并重试。")
                .setPositiveButton("重试", (dialog, which) -> {
                    dialog.dismiss();
                    if (onRetry != null) {
                        onRetry.run();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * 获取 API 错误消息
     */
    public static String getApiErrorMessage(Response<?> response) {
        if (response == null) {
            return "未知错误";
        }

        int statusCode = response.code();
        switch (statusCode) {
            case 400:
                return "请求参数错误";
            case 401:
                return "未授权，请重新登录";
            case 403:
                return "禁止访问";
            case 404:
                return "请求的资源不存在";
            case 500:
                return "服务器内部错误";
            case 502:
                return "网关错误";
            case 503:
                return "服务不可用";
            default:
                return "请求失败，错误代码: " + statusCode;
        }
    }

    /**
     * 获取网络错误消息
     */
    public static String getNetworkErrorMessage(Throwable throwable) {
        if (throwable == null) {
            return "网络错误";
        }

        String message = throwable.getMessage();
        if (message == null) {
            message = "网络错误";
        }

        if (message.contains("timeout")) {
            return "请求超时，请检查网络连接";
        } else if (message.contains("Connection refused")) {
            return "无法连接到服务器";
        } else if (message.contains("Network is unreachable")) {
            return "网络不可达";
        } else if (message.contains("Unable to resolve host")) {
            return "无法解析主机名";
        }

        return "网络错误: " + message;
    }

    /**
     * 处理 API 响应错误
     */
    public static void handleApiError(Activity activity, Response<?> response, Runnable onRetry) {
        String errorMessage = getApiErrorMessage(response);
        
        if (response.code() == 401) {
            // 未授权，需要重新登录
            showErrorDialog(activity, "会话过期", "您的登录已过期，请重新登录", () -> {
                // 清除登录信息
                SessionManager sessionManager = new SessionManager(activity);
                sessionManager.clearLoginInfo();
                
                // 跳转到登录页面
                activity.startActivity(new android.content.Intent(activity, 
                    com.example.campusbooktrading.activities.LoginActivity.class));
                activity.finish();
            });
        } else {
            showRetryDialog(activity, errorMessage, onRetry);
        }
    }

    /**
     * 处理网络错误
     */
    public static void handleNetworkError(Activity activity, Throwable throwable, Runnable onRetry) {
        String errorMessage = getNetworkErrorMessage(throwable);
        showRetryDialog(activity, errorMessage, onRetry);
    }
}
