package com.archive.app.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.archive.app.ApiService;
import com.archive.app.MyApplication;
import com.archive.app.R;

import com.archive.app.RetrofitClient;
import com.archive.app.model.Course;
import com.archive.app.model.NotificationDTO;
import com.archive.app.model.StartCheckinRequest;
import com.archive.app.view.activity.CourseDetailActivity;
import com.archive.app.view.adapter.CourseAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseManagementFragment extends Fragment implements CourseAdapter.OnCourseClickListener {

    private static final int REQUEST_CODE_COURSE_DETAIL = 101;

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddCourse;
    private ProgressBar progressBar; // 用于初始加载
    private TextView tvStatusMessage;

    private CourseAdapter adapter;
    private final List<Course> courseList = new ArrayList<>();
    private ApiService apiService;
    private Long teacherId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 使用你之前创建的 fragment_course_management.xml
        return inflater.inflate(R.layout.fragment_course_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化
        apiService = RetrofitClient.getMainApiService();
        teacherId = (long) MyApplication.curUser.getId();

        // 绑定视图
        recyclerView = view.findViewById(R.id.rv_courses);
        fabAddCourse = view.findViewById(R.id.fab_add_course);
        progressBar = view.findViewById(R.id.progress_bar); // 假设你的主布局里有
        tvStatusMessage = view.findViewById(R.id.tv_status_message); // 假设你的主布局里有

        setupRecyclerView();
        setupFab();

        loadCourses();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CourseAdapter(getContext(), courseList, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        fabAddCourse.setOnClickListener(v -> showCreateCourseDialog());
    }

    private void loadCourses() {
        showLoading();
        if (teacherId == -1) {
            handleFailure("请先登录");
            return;
        }

        apiService.getCoursesByTeacher(teacherId).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course>> call, @NonNull Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        showEmpty();
                    } else {
                        courseList.clear();
                        courseList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        showContent();
                    }
                } else {
                    handleFailure("加载课程失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Course>> call, @NonNull Throwable t) {
                handleFailure("网络错误: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCourseClick(Course course) {
        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
        intent.putExtra("COURSE_ID", course.getId());
        startActivityForResult(intent, REQUEST_CODE_COURSE_DETAIL);
    }

    @Override
    public void onStartCheckinClick(Course course) {
        showStartCheckinDialog(course);
    }

    private void showCreateCourseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_course, null);
        final TextInputEditText etCourseName = dialogView.findViewById(R.id.et_dialog_course_name);
        final TextInputEditText etCourseCode = dialogView.findViewById(R.id.et_dialog_course_code);

        builder.setView(dialogView)
                .setPositiveButton("创建", (dialog, which) -> {
                    String name = etCourseName.getText().toString().trim();
                    String code = etCourseCode.getText().toString().trim();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(getContext(), "课程名称不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    createNewCourse(name, code);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void createNewCourse(String name, String code) {
        Course newCourse = new Course();
        newCourse.setCourseName(name);
        newCourse.setCourseCode(code);
        // 重要: 关联当前教师
        newCourse.setTeacherId(teacherId);

        apiService.createCourse(newCourse).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(@NonNull Call<Course> call, @NonNull Response<Course> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "课程创建成功", Toast.LENGTH_SHORT).show();
                    loadCourses(); // 刷新列表
                } else {
                    Toast.makeText(getContext(), "创建失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Course> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showStartCheckinDialog(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_start_checkin, null);
        final TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        final TextInputEditText etDuration = dialogView.findViewById(R.id.et_dialog_duration);

        tvTitle.setText("为《" + course.getCourseName() + "》发起签到");

        builder.setView(dialogView)
                .setPositiveButton("发起", (dialog, which) -> {
                    String durationStr = etDuration.getText().toString();
                    if (TextUtils.isEmpty(durationStr)) {
                        Toast.makeText(getContext(), "请输入持续时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int duration = Integer.parseInt(durationStr);
                    startCheckin(course, duration);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void startCheckin(Course course, int duration) {
        StartCheckinRequest request = new StartCheckinRequest();
        request.setCourseId(course.getId());
        request.setDurationInMinutes(duration);
        // TODO: 从配置或地图选择获取地理围栏
        request.setLocationPolygon("30.38480,114.19810;30.38480,114.19830;30.38460,114.19830;30.38460,114.19810");

        apiService.startCheckin(request).enqueue(new Callback<NotificationDTO>() {
            @Override
            public void onResponse(@NonNull Call<NotificationDTO> call, @NonNull Response<NotificationDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "《" + course.getCourseName() + "》签到已发起！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "发起失败: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationDTO> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "网络错误，发起失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 从详情页返回后刷新列表
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COURSE_DETAIL && resultCode == Activity.RESULT_OK) {
            // 意味着在详情页进行了修改或删除，需要刷新列表
            loadCourses();
        }
    }

    // --- UI State Management ---
    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvStatusMessage.setVisibility(View.GONE);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        tvStatusMessage.setVisibility(View.GONE);
    }

    private void showEmpty() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvStatusMessage.setText("您还没有创建任何课程");
        tvStatusMessage.setVisibility(View.VISIBLE);
    }

    private void handleFailure(String message) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvStatusMessage.setText(message);
        tvStatusMessage.setVisibility(View.VISIBLE);
    }
}