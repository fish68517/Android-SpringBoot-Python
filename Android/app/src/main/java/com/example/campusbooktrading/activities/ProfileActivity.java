package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.User;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户个人资料 Activity - 显示和编辑用户信息
 */
public class ProfileActivity extends BaseActivity {


    private TextInputLayout usernameInputLayout;
    private TextInputEditText usernameInput;
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailInput;
    private TextInputLayout studentIdInputLayout;
    private TextInputEditText studentIdInput;
    private SwitchMaterial notificationsSwitch;
    private SwitchMaterial emailNotificationsSwitch;
    private SwitchMaterial privacySwitch;
    private MaterialButton changePasswordBtn;
    private MaterialButton cancelBtn;
    private MaterialButton saveBtn;
    private ProgressBar loadingProgressBar;

    private SessionManager sessionManager;
    private ApiService apiService;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 初始化
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();

        // 检查登录状态
        if (!sessionManager.isLoggedIn()) {
            ErrorHandler.showSnackbar(this, "请先登录");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 绑定视图
        bindViews();

        // 设置按钮监听器
        setupButtonListeners();

        // 加载用户数据
        loadUserProfile();
    }

    /**
     * 绑定视图
     */
    private void bindViews() {

        usernameInputLayout = findViewById(R.id.username_input_layout);
        usernameInput = findViewById(R.id.username_input);
        emailInputLayout = findViewById(R.id.email_input_layout);
        emailInput = findViewById(R.id.email_input);
        studentIdInputLayout = findViewById(R.id.student_id_input_layout);
        studentIdInput = findViewById(R.id.student_id_input);
        notificationsSwitch = findViewById(R.id.notifications_switch);
        emailNotificationsSwitch = findViewById(R.id.email_notifications_switch);
        privacySwitch = findViewById(R.id.privacy_switch);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        saveBtn = findViewById(R.id.save_btn);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
    }

    /**
     * 设置按钮监听器
     */
    private void setupButtonListeners() {
        changePasswordBtn.setOnClickListener(v -> showChangePasswordDialog());
        cancelBtn.setOnClickListener(v -> finish());
        saveBtn.setOnClickListener(v -> updateUserProfile());
    }

    /**
     * 加载用户个人资料
     */
    private void loadUserProfile() {
        String token = sessionManager.getAuthorizationHeader();
        if (token.isEmpty()) {
            ErrorHandler.showSnackbar(this, "认证令牌缺失");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::loadUserProfile);
            return;
        }

        showProgressBar(loadingProgressBar);

        apiService.getUserProfile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressBar(loadingProgressBar);

                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    displayUserProfile(currentUser);
                } else {
                    ErrorHandler.handleApiError(ProfileActivity.this, response, ProfileActivity.this::loadUserProfile);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressBar(loadingProgressBar);
                ErrorHandler.handleNetworkError(ProfileActivity.this, t, ProfileActivity.this::loadUserProfile);
            }
        });
    }

    /**
     * 显示用户个人资料
     */
    private void displayUserProfile(User user) {
        usernameInput.setText(user.username);
        emailInput.setText(user.email);
        studentIdInput.setText(user.studentId);

        // 设置开关状态（从 SharedPreferences 或默认值）
        notificationsSwitch.setChecked(true);
        emailNotificationsSwitch.setChecked(true);
        privacySwitch.setChecked(false);
    }

    /**
     * 更新用户个人资料
     */
    private void updateUserProfile() {
        String email = emailInput.getText().toString().trim();

        // 验证邮箱
        if (email.isEmpty()) {
            emailInputLayout.setError("邮箱不能为空");
            return;
        }

        if (!isValidEmail(email)) {
            emailInputLayout.setError("邮箱格式不正确");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::updateUserProfile);
            return;
        }

        showLoadingDialog("正在更新个人资料...");

        // 创建更新请求
        User updatedUser = new User();
        updatedUser.email = email;

        String token = sessionManager.getAuthorizationHeader();
        apiService.updateUserProfile(token, updatedUser).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideLoadingDialog();

                if (response.isSuccessful()) {
                    ErrorHandler.showSnackbar(ProfileActivity.this, "个人资料已更新");
                    finish();
                } else {
                    ErrorHandler.handleApiError(ProfileActivity.this, response, ProfileActivity.this::updateUserProfile);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideLoadingDialog();
                ErrorHandler.handleNetworkError(ProfileActivity.this, t, ProfileActivity.this::updateUserProfile);
            }
        });
    }

    /**
     * 显示修改密码对话框
     */
    private void showChangePasswordDialog() {
        // 创建密码输入视图
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        TextInputEditText oldPasswordInput = dialogView.findViewById(R.id.old_password_input);
        TextInputEditText newPasswordInput = dialogView.findViewById(R.id.new_password_input);
        TextInputEditText confirmPasswordInput = dialogView.findViewById(R.id.confirm_password_input);

        new MaterialAlertDialogBuilder(this)
                .setTitle("修改密码")
                .setView(dialogView)
                .setPositiveButton("确认", (dialog, which) -> {
                    String oldPassword = oldPasswordInput.getText().toString().trim();
                    String newPassword = newPasswordInput.getText().toString().trim();
                    String confirmPassword = confirmPasswordInput.getText().toString().trim();

                    // 验证输入
                    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(ProfileActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        Toast.makeText(ProfileActivity.this, "新密码不一致", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (newPassword.length() < 6) {
                        Toast.makeText(ProfileActivity.this, "密码长度至少为 6 位", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    changePassword(oldPassword, newPassword);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 修改密码
     */
    private void changePassword(String oldPassword, String newPassword) {
        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, () -> changePassword(oldPassword, newPassword));
            return;
        }

        showLoadingDialog("正在修改密码...");

        Map<String, String> passwordMap = new HashMap<>();
        passwordMap.put("old_password", oldPassword);
        passwordMap.put("new_password", newPassword);

        String token = sessionManager.getAuthorizationHeader();
        apiService.changePassword(token, passwordMap).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                hideLoadingDialog();

                if (response.isSuccessful()) {
                    ErrorHandler.showSnackbar(ProfileActivity.this, "密码已修改");
                } else {
                    ErrorHandler.handleApiError(ProfileActivity.this, response, () -> changePassword(oldPassword, newPassword));
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                hideLoadingDialog();
                ErrorHandler.handleNetworkError(ProfileActivity.this, t, () -> changePassword(oldPassword, newPassword));
            }
        });
    }

    /**
     * 验证邮箱格式
     */
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * 刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }
}
