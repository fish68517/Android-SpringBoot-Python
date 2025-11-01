package com.archive.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.archive.app.ApiService;
import com.archive.app.MyApplication;
import com.archive.app.R;

import com.archive.app.RetrofitClient;
import com.archive.app.model.CheckinRecord;
import com.archive.app.view.adapter.CheckinHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckinHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private CheckinHistoryAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvStatusMessage;
    private List<CheckinRecord> recordList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkin_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化视图
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvStatusMessage = view.findViewById(R.id.tvStatusMessage);

        // 设置 RecyclerView
        setupRecyclerView();

        // 获取并显示签到记录
        fetchCheckinHistory();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CheckinHistoryAdapter(getContext(), recordList);
        recyclerView.setAdapter(adapter);
    }

    private void fetchCheckinHistory() {
        showLoading(true);

        // 从 SessionManager 获取当前登录用户的ID
        // 注意: 你需要自己实现 SessionManager 来保存和获取登录信息
        long currentUserId = MyApplication.curUser.getId();
        if (currentUserId == -1) {
            Toast.makeText(getContext(), "用户未登录", Toast.LENGTH_SHORT).show();
            showLoading(false);
            tvStatusMessage.setText("请先登录");
            tvStatusMessage.setVisibility(View.VISIBLE);
            return;
        }

        ApiService apiService = RetrofitClient.getMainApiService();
        Call<List<CheckinRecord>> call = apiService.getCheckinHistory(currentUserId);

        call.enqueue(new Callback<List<CheckinRecord>>() {
            @Override
            public void onResponse(@NonNull Call<List<CheckinRecord>> call, @NonNull Response<List<CheckinRecord>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        // 列表为空
                        tvStatusMessage.setText("暂无签到记录");
                        tvStatusMessage.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        // 成功获取数据
                        recordList.clear();
                        recordList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        tvStatusMessage.setVisibility(View.GONE);
                    }
                } else {
                    // 请求成功但业务失败
                    handleFailure("加载失败，请稍后重试");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CheckinRecord>> call, @NonNull Throwable t) {
                // 网络请求失败
                showLoading(false);
                handleFailure("网络错误: " + t.getMessage());
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tvStatusMessage.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void handleFailure(String message) {
        tvStatusMessage.setText(message);
        tvStatusMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}