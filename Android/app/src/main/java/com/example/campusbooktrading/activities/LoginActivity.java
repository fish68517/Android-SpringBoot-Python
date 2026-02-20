package com.example.campusbooktrading.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.campusbooktrading.R;
import com.example.campusbooktrading.api.ApiClient;
import com.example.campusbooktrading.api.ApiService;
import com.example.campusbooktrading.models.AuthRequest;
import com.example.campusbooktrading.models.AuthResponse;
import com.example.campusbooktrading.utils.ErrorHandler;
import com.example.campusbooktrading.utils.NetworkUtils;
import com.example.campusbooktrading.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录 Activity
 */
public class LoginActivity extends BaseActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView registerLink;
    private CheckBox rememberMeCheckbox; // 新增 CheckBox

    private SessionManager sessionManager;
    private ApiService apiService;

    // SharedPreferences 相关的常量定义
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_REMEMBER_ME = "is_remember_me";
    private static final String KEY_EMAIL = "saved_email";
    private static final String KEY_PASSWORD = "saved_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // 绑定视图
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);

        // 设置监听器
        loginButton.setOnClickListener(v -> performLogin());
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // 检查是否勾选了记住密码，如果是，则自动填充
        checkAndFillCredentials();
    }

    /**
     * 检查并自动填充保存的凭证
     */
    private void checkAndFillCredentials() {
        boolean isRemembered = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        if (isRemembered) {
            rememberMeCheckbox.setChecked(true);
            emailInput.setText(sharedPreferences.getString(KEY_EMAIL, ""));
            passwordInput.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
        }
    }

    /**
     * 执行登录
     */
    private void performLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // 验证输入
        if (email.isEmpty() || password.isEmpty()) {
            ErrorHandler.showSnackbar(this, "请输入邮箱和密码");
            return;
        }

        // 检查网络连接
        if (!NetworkUtils.isNetworkConnected(this)) {
            ErrorHandler.showNetworkErrorDialog(this, this::performLogin);
            return;
        }

        // 显示加载状态
        loginButton.setEnabled(false);
        loginButton.setText("登录中...");

        // 调用 API
        AuthRequest request = new AuthRequest(email, password);
        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                loginButton.setEnabled(true);
                loginButton.setText(getString(R.string.login));

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {

                        // 登录成功后，处理“记住密码”逻辑
                        handleRememberMe(email, password);

                        // 保存登录信息
                        sessionManager.saveLoginInfo(
                                authResponse.userId,
                                authResponse.username,
                                authResponse.email,
                                authResponse.studentId,
                                authResponse.token
                        );

                        // 跳转到首页
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        ErrorHandler.showSnackbar(LoginActivity.this, authResponse.error);
                    }
                } else {
                    ErrorHandler.handleApiError(LoginActivity.this, response, LoginActivity.this::performLogin);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loginButton.setEnabled(true);
                loginButton.setText(getString(R.string.login));
                ErrorHandler.handleNetworkError(LoginActivity.this, t, LoginActivity.this::performLogin);
            }
        });
    }

    /**
     * 处理记住密码配置的保存或清除
     */
    private void handleRememberMe(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMeCheckbox.isChecked()) {
            // 勾选则保存数据
            editor.putBoolean(KEY_REMEMBER_ME, true);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PASSWORD, password);
        } else {
            // 取消勾选则清除密码数据（这里保留了邮箱体验更好，但您可以根据需求选择清除邮箱）
            editor.putBoolean(KEY_REMEMBER_ME, false);
            editor.remove(KEY_PASSWORD);
            // editor.remove(KEY_EMAIL); // 如果希望取消勾选时邮箱也不记录，可以取消注释此行
        }
        editor.apply();
    }
}