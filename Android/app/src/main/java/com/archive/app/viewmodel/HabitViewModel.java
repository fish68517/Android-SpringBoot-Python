package com.archive.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.archive.app.ApiClient;
import com.archive.app.ApiService;
import com.archive.app.model.HabitTrack;

import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabitViewModel extends ViewModel {

    private final MutableLiveData<List<HabitTrack>> userHabits = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final Long currentUserId = 1L;

    public LiveData<List<HabitTrack>> getUserHabits() { return userHabits; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public HabitViewModel() {
        fetchUserHabits();
    }

    private void fetchUserHabits() {
        isLoading.setValue(true);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<HabitTrack>> call = apiService.getAllHabitTracks();

        call.enqueue(new Callback<List<HabitTrack>>() {
            @Override
            public void onResponse(Call<List<HabitTrack>> call, Response<List<HabitTrack>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HabitTrack> filteredHabits = response.body().stream()
                            .filter(habit -> currentUserId.equals(habit.getCampusUserId()))
                            .collect(Collectors.toList());
                    userHabits.setValue(filteredHabits);
                } else {
                    errorMessage.setValue("加载习惯列表失败");
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<HabitTrack>> call, Throwable t) {
                errorMessage.setValue("网络错误：" + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }
}