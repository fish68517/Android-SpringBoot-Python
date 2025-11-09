package com.archive.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.archive.app.ApiService;
import com.archive.app.MyApplication;
import com.archive.app.RetrofitClient;
import com.archive.app.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<User> userProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final Long currentUserId = (long) MyApplication.curUser.getId();

    public LiveData<User> getUserProfile() { return userProfile; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public ProfileViewModel() {
        fetchUserProfile();
    }

    public void fetchUserProfile() {
        isLoading.setValue(true);
        ApiService apiService = RetrofitClient.getMainApiService();
        Call<User> call = apiService.getCampusUserById(currentUserId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userProfile.setValue(response.body());
                } else {
                    errorMessage.setValue("加载个人信息失败");
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorMessage.setValue("网络错误：" + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }
}