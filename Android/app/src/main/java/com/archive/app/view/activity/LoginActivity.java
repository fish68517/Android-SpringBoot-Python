// LoginActivity.java
package com.archive.app.view.activity; // 请替换为您的实际包名

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.archive.app.ApiService;
import com.archive.app.MyApplication;
import com.archive.app.RetrofitClient;

import com.archive.app.R; // 引入您的 R 文件
import com.archive.app.model.User;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // 1. 定义UI控件变量
    private Toolbar toolbar;
    private TextInputEditText etUsername;
    private ApiService apiService = RetrofitClient.getMainApiService();

    private TextInputEditText etPassword;

    private RadioGroup rgLoginRole; // <-- 新增
    private RadioButton rbLoginStudent; // <-- 新增

    private CheckBox cbRememberPassword;

    // 2. 定义数据操作对象
    private SharedPreferences sharedPreferences;
    private Button btnLogin;
    private Button btnToRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化 DAO 和 SharedPreferences

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        // 初始化视图控件
        initViews();

        // 设置点击事件监听
        setupListeners();

        // 检查并加载已保存的密码
        loadRememberedPassword();
    }

    /**
     * 绑定XML布局中的控件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar_login);
        etUsername = findViewById(R.id.et_login_username);
        etPassword = findViewById(R.id.et_login_password);
        cbRememberPassword = findViewById(R.id.cb_remember_password);
        btnLogin = findViewById(R.id.btn_login);
        btnToRegister = findViewById(R.id.btn_to_register);
        findViewById(R.id.btn_to_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FaceMainActivity.class);
                startActivity(intent);
            }
        });

        // 设置 Toolbar
        setSupportActionBar(toolbar);
    }

    /**
     * 设置所有点击事件
     */
    private void setupListeners() {
        // 登录按钮的点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // “去注册”按钮的点击事件
        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到注册页面 (假设您的注册Activity叫 RegisterActivity)
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 从 SharedPreferences 加载已记住的密码
     */
    private void loadRememberedPassword() {
        boolean isRemember = sharedPreferences.getBoolean("is_remember", false);
        if (isRemember) {
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");
            etUsername.setText(username);
            etPassword.setText(password);
            cbRememberPassword.setChecked(true);
        }
    }

    /**
     * 处理登录逻辑的核心方法
     */
    private void handleLogin() {
        // 1. 获取输入框中的用户名和密码
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // --- 新增：获取选中的角色 ---
        int selectedRoleId = rgLoginRole.getCheckedRadioButtonId();
        boolean isStudentLogin = (selectedRoleId == R.id.rb_login_student);

        // 2. 输入验证
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            etUsername.requestFocus(); // 将光标定位到用户名输入框
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        apiService.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User userFromDb = response.body();

                    // --- 核心角色检查 ---
                    // 假设 1=学生, 2=教师/管理员
                    boolean isUserStudent = (userFromDb.getRole() == 1);
                    boolean isUserTeacher = (userFromDb.getRole() == 2);

                    // 检查用户选择的角色和实际角色是否匹配
                    if (isStudentLogin && !isUserStudent) {
                        Toast.makeText(LoginActivity.this, "登录失败：您是教师，请选择教师角色登录", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!isStudentLogin && !isUserTeacher) {
                        Toast.makeText(LoginActivity.this, "登录失败：您是学生，请选择学生角色登录", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // --- 角色匹配，登录成功 ---
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    handleRememberPassword(username, password);
                    MyApplication.curUser = userFromDb;

                    // --- 根据角色跳转到不同界面 ---
                    Intent intent;
                    if (isUserStudent) {
                        // 学生跳转到 MainActivity
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                    } else {
                        // 教师跳转到 TeacherMainActivity (新)
                        intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                        intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.e("LoginActivity", "登录失败：" + t.getMessage());
                Toast.makeText(LoginActivity.this, "登录失败！请检查用户名和密码", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 处理“记住密码”的逻辑
     * @param username 用户名
     * @param password 密码
     */
    private void handleRememberPassword(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (cbRememberPassword.isChecked()) {
            // 如果勾选了“记住密码”
            editor.putBoolean("is_remember", true);
            editor.putString("username", username);
            editor.putString("password", password);
        } else {
            // 如果没有勾选，则清除保存的记录
            editor.clear();
        }
        editor.apply(); // 提交更改
    }
}