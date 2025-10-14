package com.archive.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.archive.app.ApiService;
import com.archive.app.RetrofitClient;
import com.archive.app.R;
import com.archive.app.model.CampusUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private RadioGroup rgRole;
    private Button btnRegister;
    private ApiService apiService;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);

        etUsername = findViewById(R.id.et_register_username);
        etPassword = findViewById(R.id.et_register_password);
        rgRole = findViewById(R.id.rg_register_role);
        btnRegister = findViewById(R.id.btn_register);

        apiService = RetrofitClient.getMainApiService();

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = rgRole.getCheckedRadioButtonId() == R.id.rb_register_user ? "student" : "admin";
            System.out.println(username + " " + password + " 角色： " + role);
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            CampusUser user = new CampusUser();
            user.setCampusEmailAddr(username);
            user.setPassword(password);
            user.setCampusUserType(role);

            apiService.register(user).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body());
                        Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    Toast.makeText(RegisterActivity.this, "注册失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });

        });
    }
}