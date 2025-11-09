package com.archive.app.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.model.LatLng;
import com.archive.app.ApiService;
import com.archive.app.MyApplication;
import com.archive.app.R;
import com.archive.app.RetrofitClient;
import com.archive.app.model.Course;
import com.archive.app.model.NotificationDTO;
import com.archive.app.model.User;
import com.archive.app.view.activity.GeofenceSelectorActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartCheckinFragment extends Fragment {

    private static final String TAG = "StartCheckinFragment";

    private Spinner spinnerCourses;
    private EditText etDuration;
    private TextView tvGeofenceStatus;
    private Button btnSelectGeofence;
    private Button btnStartCheckin;

    private ApiService apiService;
    private List<Course> teacherCourses = new ArrayList<>();

    // 存储从地图Activity返回的数据
    private String selectedPolygon;
    private double selectedThreshold;


    // Activity Result Launcher, 用于接收 GeofenceSelectorActivity 的返回结果
    private final ActivityResultLauncher<Intent> geofenceLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    selectedPolygon = data.getStringExtra(GeofenceSelectorActivity.RESULT_POLYGON_STRING);
                    // json 格式 selectedPolygon
                    //  Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $
                    //selectedPolygonList = Collections.singletonList(new Gson().fromJson(selectedPolygon, LatLng.class));

                    selectedThreshold = data.getDoubleExtra(GeofenceSelectorActivity.RESULT_THRESHOLD, 10.0);

                    if(selectedPolygon != null && !selectedPolygon.isEmpty()) {
                        Log.i(TAG, "地理围栏已设置: " + selectedPolygon);
                        tvGeofenceStatus.setText("状态: 已设置 (" + selectedPolygon + " )");
                        tvGeofenceStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_checkin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerCourses = view.findViewById(R.id.spinner_courses);
        etDuration = view.findViewById(R.id.et_duration);
        tvGeofenceStatus = view.findViewById(R.id.tv_geofence_status);
        btnSelectGeofence = view.findViewById(R.id.btn_select_geofence);
        btnStartCheckin = view.findViewById(R.id.btn_start_checkin);

        apiService = RetrofitClient.getMainApiService();

        loadTeacherCourses();

        btnSelectGeofence.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GeofenceSelectorActivity.class);
            geofenceLauncher.launch(intent);
        });

        btnStartCheckin.setOnClickListener(v -> {
            startCheckin();
        });
    }

    private void loadTeacherCourses() {
        User teacher = MyApplication.curUser;
        if (teacher == null) {
            Toast.makeText(getContext(), "无法获取教师信息", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getCoursesByTeacher((long) teacher.getId()).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    teacherCourses = response.body();
                    List<String> courseNames = new ArrayList<>();
                    for (Course course : teacherCourses) {
                        courseNames.add(course.getCourseName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, courseNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCourses.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "加载课程列表失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCheckin() {
        // 1. 数据校验
        if (spinnerCourses.getSelectedItem() == null) {
            Toast.makeText(getContext(), "请选择一门课程", Toast.LENGTH_SHORT).show();
            return;
        }
        String durationStr = etDuration.getText().toString().trim();
        if (durationStr.isEmpty()) {
            Toast.makeText(getContext(), "请输入签到时长", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedPolygon == null || selectedPolygon.isEmpty()) {
            Toast.makeText(getContext(), "请先在地图上设置地理围栏", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 组装 DTO
        Course selectedCourse = teacherCourses.get(spinnerCourses.getSelectedItemPosition());
        int durationMinutes = Integer.parseInt(durationStr);
        User teacher = MyApplication.curUser;

        NotificationDTO dto = new NotificationDTO();
        dto.setCourseId(selectedCourse.getId());
        dto.setLocationPolygon(selectedPolygon);
        dto.setThresholdMeters(selectedThreshold);
        dto.setStatus("ACTIVE");

        Log.d(TAG, "发起签到: CourseID=" + dto.getCourseId() + ", Duration="  + ", Polygon=" + dto.getLocationPolygon());

        // 3. 发送 API 请求
        btnStartCheckin.setEnabled(false);
        btnStartCheckin.setText("发起中...");

        apiService.startCheckin(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnStartCheckin.setEnabled(true);
                btnStartCheckin.setText("发起签到");
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "签到发起成功！", Toast.LENGTH_LONG).show();
                    // 可以在此清空界面
                    etDuration.setText("");
                    tvGeofenceStatus.setText("状态: 未设置");
                    selectedPolygon = null;
                } else {
                    Toast.makeText(getContext(), "发起失败: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnStartCheckin.setEnabled(true);
                btnStartCheckin.setText("发起签到");
                Toast.makeText(getContext(), "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}