package com.archive.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.archive.app.ApiClient;
import com.archive.app.ApiService;
import com.archive.app.model.TaskFocus;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskViewModel extends ViewModel {

    // 使用 LiveData 来持有UI状态，Fragment可以观察它
    private final MutableLiveData<List<TaskFocus>> userTasks = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // 假设当前登录的用户ID为1 ("张伟")
    private final Long currentUserId = 1L;

    public LiveData<List<TaskFocus>> getUserTasks() {
        return userTasks;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // ViewModel 创建时就去加载数据
    public TaskViewModel() {
        fetchUserTasks();
    }

    private void fetchUserTasks() {
        isLoading.setValue(true); // 开始加载，通知UI显示进度条

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<TaskFocus>> call = apiService.getAllTaskFocuses();

        call.enqueue(new Callback<List<TaskFocus>>() {
            @Override
            public void onResponse(Call<List<TaskFocus>> call, Response<List<TaskFocus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 筛选出当前用户的任务
                    List<TaskFocus> filteredTasks = response.body().stream()
                            .filter(task -> currentUserId.equals(task.getCampusUserId()))
                            .collect(Collectors.toList());
                    userTasks.setValue(filteredTasks); // 更新数据
                } else {
                    errorMessage.setValue("加载任务列表失败，请重试");
                }
                isLoading.setValue(false); // 加载结束，隐藏进度条
            }

            @Override
            public void onFailure(Call<List<TaskFocus>> call, Throwable t) {
                errorMessage.setValue("网络错误：" + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }
}