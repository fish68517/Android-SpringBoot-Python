package com.archive.app.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.archive.app.ApiService;
import com.archive.app.MyApplication;
import com.archive.app.R;

import com.archive.app.RetrofitClient;
import com.archive.app.model.CheckinStatistics;
import com.archive.app.model.Course;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsFragment extends Fragment {

    private Spinner spinnerCourses;
    private MaterialButtonToggleGroup toggleGroup;
    private BarChart barChart;
    private ProgressBar progressBar;
    private TextView tvStatusMessage;

    private ApiService apiService;
    private Long teacherId;
    private List<Course> courseList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        apiService = RetrofitClient.getMainApiService();
        teacherId = (long) MyApplication.curUser.getId();

        setupSpinner();
        setupToggleGroup();
        setupChart();

        loadCoursesForSpinner();
    }

    private void findViews(View view) {
        spinnerCourses = view.findViewById(R.id.spinner_courses);
        toggleGroup = view.findViewById(R.id.toggle_group_time);
        barChart = view.findViewById(R.id.bar_chart);
        progressBar = view.findViewById(R.id.progress_bar);
        tvStatusMessage = view.findViewById(R.id.tv_status_message);
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(spinnerAdapter);

        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchStatistics(); // 当选择新课程时，刷新图表
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupToggleGroup() {
        toggleGroup.check(R.id.btn_day); // 默认选中 "日"
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                fetchStatistics(); // 当切换时间维度时，刷新图表
            }
        });
    }

    private void loadCoursesForSpinner() {
        showLoading(true);
        tvStatusMessage.setText("正在加载课程列表...");
        apiService.getCoursesByTeacher(teacherId).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course>> call, @NonNull Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseList = response.body();
                    List<String> courseNames = new ArrayList<>();
                    for (Course course : courseList) {
                        courseNames.add(course.getCourseName());
                    }
                    spinnerAdapter.clear();
                    spinnerAdapter.addAll(courseNames);
                    spinnerAdapter.notifyDataSetChanged();

                    if (courseList.isEmpty()) {
                        tvStatusMessage.setText("您还没有创建课程");
                        showStatusMessage(true);
                    } else {
                        showStatusMessage(false);
                    }
                } else {
                    tvStatusMessage.setText("加载课程列表失败");
                    showStatusMessage(true);
                }
                showLoading(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Course>> call, @NonNull Throwable t) {
                tvStatusMessage.setText("网络错误");
                showStatusMessage(true);
                showLoading(false);
            }
        });
    }

    private void fetchStatistics() {
        if (courseList.isEmpty()) return;

        showLoading(true);
        barChart.setVisibility(View.INVISIBLE);

        int selectedPosition = spinnerCourses.getSelectedItemPosition();
        Course selectedCourse = courseList.get(selectedPosition);
        long courseId = selectedCourse.getId();

        int checkedId = toggleGroup.getCheckedButtonId();

        // 使用 SimpleDateFormat 获取当前日期/周/月的字符串表示
        // ... (你需要实现这部分逻辑)
        // 获取当前日期时间
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

// 创建不同格式的日期格式化器
        SimpleDateFormat dailyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat weeklyFormat = new SimpleDateFormat("yyyy-'W'ww", Locale.CHINA);
        SimpleDateFormat monthlyFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);

// 根据选择的时间维度获取相应格式的日期字符串
        Call<CheckinStatistics> apiCall;
        if (checkedId == R.id.btn_day) {
            String dailyString = dailyFormat.format(currentDate);
            apiCall = apiService.getDailyStatistics(courseId, dailyString);
        } else if (checkedId == R.id.btn_week) {
            String weeklyString = weeklyFormat.format(currentDate);
            apiCall = apiService.getWeeklyStatistics(courseId, weeklyString);
        } else { // month
            String monthlyString = monthlyFormat.format(currentDate);
            apiCall = apiService.getMonthlyStatistics(courseId, monthlyString);
        }

        apiCall.enqueue(new Callback<CheckinStatistics>() {
            @Override
            public void onResponse(@NonNull Call<CheckinStatistics> call, @NonNull Response<CheckinStatistics> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    barChart.setVisibility(View.VISIBLE);
                    updateChart(response.body());
                } else {
                    Toast.makeText(getContext(), "获取统计数据失败", Toast.LENGTH_SHORT).show();
                    barChart.clear();
                }
            }
            @Override
            public void onFailure(@NonNull Call<CheckinStatistics> call, @NonNull Throwable t) {
                showLoading(false);
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                barChart.clear();
            }
        });
    }

    private void setupChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // 确保标签不重叠

        // final String[] labels = new String[]{"成功", "迟到", "缺勤"};
        final String[] labels = new String[]{"成功", "失败"};
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels[(int) value];
            }
        });

        barChart.getAxisRight().setEnabled(false); // 禁用右侧Y轴
        barChart.getAxisLeft().setAxisMinimum(0f); // Y轴从0开始
    }

    private void updateChart(CheckinStatistics stats) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, stats.getPresentCount()));
        entries.add(new BarEntry(1f, stats.getLateCount()));
      //  entries.add(new BarEntry(2f, stats.getAbsentCount()));

        BarDataSet dataSet = new BarDataSet(entries, "签到人数");

        // 设置颜色
        dataSet.setColors(
                ContextCompat.getColor(getContext(), R.color.status_success),
                ContextCompat.getColor(getContext(), R.color.status_late),
                ContextCompat.getColor(getContext(), R.color.status_absent)
        );
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);

        barChart.setData(barData);
        barChart.animateY(1000); // 添加动画
        barChart.invalidate(); // 刷新
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showStatusMessage(boolean isVisible) {
        tvStatusMessage.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}