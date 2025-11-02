package com.archive.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.archive.app.ApiService;
import com.archive.app.R;
import com.archive.app.RetrofitClient;
import com.archive.app.model.Course;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout tilCourseName, tilCourseCode;
    private TextInputEditText etCourseName, etCourseCode;
    private MaterialButton btnSave, btnDelete;

    private ApiService apiService;
    private long courseId = -1;
    private Course currentCourse; // 保存当前课程的引用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // 初始化 API 服务
        apiService = RetrofitClient.getMainApiService();

        // 获取从 Fragment 传来的课程 ID
        courseId = getIntent().getLongExtra("COURSE_ID", -1);
        if (courseId == -1) {
            Toast.makeText(this, "无效的课程ID", Toast.LENGTH_SHORT).show();
            finish(); // 如果没有有效的 ID，直接关闭页面
            return;
        }

        // 初始化所有视图
        setupToolbar();
        findViews();
        setupClickListeners();

        // 从服务器加载课程详情
        loadCourseDetails();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void findViews() {
        tilCourseName = findViewById(R.id.til_course_name);
        etCourseName = findViewById(R.id.et_course_name);
        tilCourseCode = findViewById(R.id.til_course_code);
        etCourseCode = findViewById(R.id.et_course_code);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveCourse());
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void loadCourseDetails() {
        apiService.getCourseById(courseId).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(@NonNull Call<Course> call, @NonNull Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentCourse = response.body();
                    etCourseName.setText(currentCourse.getCourseName());
                    etCourseCode.setText(currentCourse.getCourseCode());
                } else {
                    Toast.makeText(CourseDetailActivity.this, "加载课程信息失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Course> call, @NonNull Throwable t) {
                Toast.makeText(CourseDetailActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCourse() {
        String newName = etCourseName.getText().toString().trim();
        String newCode = etCourseCode.getText().toString().trim();

        // 验证输入
        if (TextUtils.isEmpty(newName)) {
            tilCourseName.setError("课程名称不能为空");
            return;
        } else {
            tilCourseName.setError(null); // 清除错误提示
        }

        // 更新 currentCourse 对象
        currentCourse.setCourseName(newName);
        currentCourse.setCourseCode(newCode);

        // 调用更新接口
        apiService.updateCourse(courseId, currentCourse).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(@NonNull Call<Course> call, @NonNull Response<Course> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CourseDetailActivity.this, "课程已保存", Toast.LENGTH_SHORT).show();
                    // 设置一个结果，通知上一个页面数据已更改
                    setResult(Activity.RESULT_OK);

                    finish(); // 关闭当前页面，返回列表
                } else {
                    Toast.makeText(CourseDetailActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Course> call, @NonNull Throwable t) {
                Toast.makeText(CourseDetailActivity.this, "网络错误，保存失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("您确定要删除《" + currentCourse.getCourseName() + "》这门课程吗？此操作无法撤销。")
                .setPositiveButton("删除", (dialog, which) -> {
                    // 用户确认删除，执行删除操作
                    deleteCourse();
                })
                .setNegativeButton("取消", null) // 点击取消，对话框自动关闭
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteCourse() {
        apiService.deleteCourse(courseId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // HTTP 204 No Content 表示成功
                if (response.isSuccessful()) {
                    Toast.makeText(CourseDetailActivity.this, "课程已删除", Toast.LENGTH_SHORT).show();
                    // 同样设置结果，通知列表刷新
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(CourseDetailActivity.this, "网络错误，删除失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 处理 Toolbar 上的返回按钮点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // 模拟系统返回键的行为
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}