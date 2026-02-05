package com.example.campusbooktrading.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();

        // 绑定视图
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);

        // 设置监听器
        loginButton.setOnClickListener(v -> performLogin());
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
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
}
