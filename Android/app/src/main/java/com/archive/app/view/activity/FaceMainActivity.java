package com.archive.app.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.archive.app.FaceApiClient;
import com.archive.app.R;
import com.archive.app.view.CustomLoginDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FaceMainActivity extends AppCompatActivity {

    // !!! 非常重要 !!!
    // 替换成你电脑在局域网中的IP地址
    private static final String SERVER_URL = FaceApiClient.BASE_URL;

    private ImageView ivPhotoPreview;
    private TextView tvResult;
    private Button btnTakePhoto;
    private Button btnRegister;

    private Button btnLogin;

    private Bitmap currentBitmap;
    private final OkHttpClient client = new OkHttpClient();

    // 注册 Activity Result Launcher 来处理拍照结果
    private final ActivityResultLauncher<Void> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(),
            bitmap -> {
                if (bitmap != null) {
                    currentBitmap = bitmap;
                    ivPhotoPreview.setImageBitmap(bitmap);
                    tvResult.setText("照片已拍摄");
                    btnRegister.setEnabled(true);
                    btnLogin.setEnabled(true);
                }
            }
    );

    // --- 新增代码：用于请求权限 ---
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // 用户授予了权限，直接启动相机
                    showToast("相机权限已获取");
                    takePictureLauncher.launch(null);
                } else {
                    // 用户拒绝了权限
                    showToast("需要相机权限才能拍照");
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_face);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 隐藏默认标题
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // 在这里处理返回逻辑，例如 finish();
            onBackPressed();
        });

        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);
        tvResult = findViewById(R.id.tvResult);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        // --- 修改拍照按钮的点击事件 ---
        btnTakePhoto.setOnClickListener(v -> {
            // 在启动相机前检查权限
            checkCameraPermissionAndLaunch();
        });

        btnRegister.setOnClickListener(v -> showRegisterDialog());

        btnLogin.setOnClickListener(v -> login());
    }

    // --- 新增方法：检查权限并启动相机 ---
    private void checkCameraPermissionAndLaunch() {
        // 检查应用是否已经获得了 CAMERA 权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // 如果已经有权限，直接启动相机
            takePictureLauncher.launch(null);
        } else {
            // 如果没有权限，发起权限请求
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void showRegisterDialog() {
        if (currentBitmap == null) {
            showToast("请先拍照");
            return;
        }

        CustomLoginDialog dialog = new CustomLoginDialog(this);
        dialog.setOnConfirmListener((username, password) -> {
            register(username, password); // 如果需要调整参数
        });
        dialog.show();
    }

    private void register(String userName,String password) {
        runOnUiThread(() -> tvResult.setText("注册中..."));
        if (currentBitmap == null) return;

        // 将 Bitmap 转换为 ByteArray
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byteArray = stream.toByteArray();

        // 创建 Multipart 请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userName)
                .addFormDataPart("image", "face.jpg",
                        RequestBody.create(byteArray, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(SERVER_URL + "/register")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    String errorMessage = "注册失败: " + e.getMessage();
                    tvResult.setText(errorMessage);
                    showToast(errorMessage);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        String errorMessage = "服务器错误: " + response;
                        tvResult.setText(errorMessage);
                        showToast(errorMessage);
                    });
                    throw new IOException("服务器错误: " + response);
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);
                    // 打印 json 内容
                    Log.d("RegisterActivity", "注册人脸返回 json: " + json);
                    String message = json.getString("message");
                    runOnUiThread(() -> {
                        // 调用Spring Boot接口
                        tvResult.setText(message);
                        showToast(message);
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        String errorMessage = "解析响应失败: " + e.getMessage();
                        tvResult.setText(errorMessage);
                        showToast(errorMessage);
                    });
                }
            }
        });
    }

    private void login() {
        runOnUiThread(() -> tvResult.setText("登录中..."));
        if (currentBitmap == null) return;

        // 将 Bitmap 转换为 ByteArray
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "face.jpg",
                        RequestBody.create(byteArray, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(SERVER_URL + "/login")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    String errorMessage = "登录失败: " + e.getMessage();
                    tvResult.setText(errorMessage);
                    showToast(errorMessage);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        String errorMessage = "服务器错误: " + response;
                        tvResult.setText(errorMessage);
                        showToast(errorMessage);
                    });
                    throw new IOException("服务器错误: " + response);
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);
                    String status = json.getString("status");

                    final String resultMessage;
                    if ("success".equals(status)) {
                        String userName = json.getString("user_id");
                        resultMessage = "登录成功！欢迎, " + userName;
                    } else {
                        resultMessage = json.getString("message");
                    }

                    runOnUiThread(() -> {
                        tvResult.setText(resultMessage);
                        showToast(resultMessage);
                        Intent intent = new Intent(FaceMainActivity.this, MainActivity.class);

                        startActivity(intent);
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        String errorMessage = "解析响应失败: " + e.getMessage();
                        tvResult.setText(errorMessage);
                        showToast(errorMessage);
                    });
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}