package com.archive.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.archive.app.ApiClient;
import com.archive.app.ApiService;
import com.archive.app.model.Achievement;
import com.archive.app.model.UserAchieveRel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementViewModel extends ViewModel {

    private final MutableLiveData<List<Achievement>> allAchievements = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final Long currentUserId = 1L;

    public LiveData<List<Achievement>> getAllAchievements() { return allAchievements; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public AchievementViewModel() {
        fetchAchievements();
    }

    private void fetchAchievements() {
        isLoading.setValue(true);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 1. 先获取所有徽章
        apiService.getAllAchievements().enqueue(new Callback<List<Achievement>>() {
            @Override
            public void onResponse(Call<List<Achievement>> call, Response<List<Achievement>> allAchievementsResponse) {
                if (!allAchievementsResponse.isSuccessful() || allAchievementsResponse.body() == null) {
                    errorMessage.setValue("加载徽章列表失败");
                    isLoading.setValue(false);
                    return;
                }
                List<Achievement> achievements = allAchievementsResponse.body();

                // 2. 再获取用户已获得的徽章关系
                apiService.getAllUserAchieveRels().enqueue(new Callback<List<UserAchieveRel>>() {
                    @Override
                    public void onResponse(Call<List<UserAchieveRel>> call, Response<List<UserAchieveRel>> userRelsResponse) {
                        if (userRelsResponse.isSuccessful() && userRelsResponse.body() != null) {
                            // 筛选出当前用户获得的徽章ID集合
                            Set<Long> earnedIds = userRelsResponse.body().stream()
                                    .filter(rel -> currentUserId.equals(rel.getCampusUserId()))
                                    .map(UserAchieveRel::getAchievementId)
                                    .collect(Collectors.toSet());

                            // 遍历所有徽章，标记是否已获得 (在模型中添加isEarned字段)
                            for(Achievement ach : achievements) {
                                // ach.setEarned(earnedIds.contains(ach.getAchievementId()));
                            }
                        }
                        allAchievements.setValue(achievements); // 更新合并后的数据
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onFailure(Call<List<UserAchieveRel>> call, Throwable t) {
                        allAchievements.setValue(achievements); // 即使关系失败，也显示所有徽章
                        isLoading.setValue(false);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Achievement>> call, Throwable t) {
                errorMessage.setValue("网络错误: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }
}