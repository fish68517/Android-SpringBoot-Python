package com.archive.app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.archive.app.R;
import com.archive.app.view.adapter.TaskAdapter;
import com.archive.app.viewmodel.TaskViewModel;


public class TaskFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TaskAdapter taskAdapter; // 需要创建一个Adapter

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 关联布局文件
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // 初始化UI组件
        recyclerView = view.findViewById(R.id.recycler_view_tasks);
        progressBar = view.findViewById(R.id.progress_bar_tasks);

        setupRecyclerView();

        // 观察ViewModel中的数据变化
        observeViewModel();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(); // 假设TaskAdapter已创建
        recyclerView.setAdapter(taskAdapter);
    }

    private void observeViewModel() {
        // 观察任务列表数据
        taskViewModel.getUserTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                taskAdapter.setTasks(tasks); // 更新Adapter的数据
            }
        });

        // 观察加载状态
        taskViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        // 观察错误信息
        taskViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}