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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.archive.app.ApiService;
import com.archive.app.MyApplication;
import com.archive.app.R;

import com.archive.app.RetrofitClient;
import com.archive.app.model.NotificationDTO;
import com.archive.app.view.activity.MainActivity;
import com.archive.app.view.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckinNotificationFragment extends Fragment implements NotificationAdapter.OnCheckinButtonClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvStatusMessage;
    private NotificationAdapter adapter;
    private final List<NotificationDTO> notificationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkin_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setupRecyclerView();
        setupSwipeRefresh();

        // 首次进入时自动加载数据
        loadNotifications(true);
    }

    private void findViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.rv_notifications);
        progressBar = view.findViewById(R.id.progress_bar);
        tvStatusMessage = view.findViewById(R.id.tv_status_message);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(getContext(), notificationList, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.purple_500, R.color.purple_700);
        swipeRefreshLayout.setOnRefreshListener(() -> loadNotifications(false));
    }

    private void loadNotifications(boolean isInitialLoad) {
        if (isInitialLoad) {
            showLoading();
        }

        long studentId = MyApplication.curUser.getId();
        if (studentId == -1) {
            handleFailure("请先登录", isInitialLoad);
            return;
        }

        ApiService apiService = RetrofitClient.getMainApiService();
        apiService.getNotificationsForStudent(studentId).enqueue(new Callback<List<NotificationDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<NotificationDTO>> call, @NonNull Response<List<NotificationDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        showEmpty();
                    } else {
                        notificationList.clear();
                        notificationList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        showContent();
                    }
                } else {
                    handleFailure("加载失败，请稍后重试", isInitialLoad);
                }
                if (!isInitialLoad) swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<NotificationDTO>> call, @NonNull Throwable t) {
                handleFailure("网络错误: " + t.getMessage(), isInitialLoad);
                if (!isInitialLoad) swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCheckinButtonClick(NotificationDTO notification) {
        // 创建 Bundle 并放入数据
        Bundle args = new Bundle();
        args.putSerializable("notification", notification);

        // 创建 MapFragment 实例
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(args);

        // 通过 FragmentManager 进行跳转
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .addToBackStack(null)
                .commit();
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
        tvStatusMessage.setText("暂无签到通知");
        tvStatusMessage.setVisibility(View.VISIBLE);
    }

    private void handleFailure(String message, boolean isInitialLoad) {
        if (isInitialLoad) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            tvStatusMessage.setText(message);
            tvStatusMessage.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}