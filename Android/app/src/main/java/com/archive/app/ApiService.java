package com.archive.app;

import com.archive.app.model.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 定义所有与后端Spring Boot的API接口 (完整版)
 */
public interface ApiService {

    //=========================== 1. 校园用户 (CampusUser) ===========================
    @POST("/campusUser/login")
    Call<CampusUser> login(@Body CampusUser user);

    @POST("/campusUser")
    Call<Boolean> register(@Body CampusUser user);

    @GET("/campusUser/{id}")
    Call<CampusUser> getCampusUserById(@Path("id") Long id);

    @GET("/campusUser/list")
    Call<List<CampusUser>> getAllCampusUsers();

    @PUT("/campusUser")
    Call<Boolean> updateCampusUser(@Body CampusUser user);

    @DELETE("/campusUser/{id}")
    Call<Boolean> deleteCampusUser(@Path("id") Long id);

    //=========================== 2. 任务专注 (TaskFocus) ===========================
    @POST("/taskFocus")
    Call<Boolean> createTaskFocus(@Body TaskFocus taskFocus);

    @GET("/taskFocus/{id}")
    Call<TaskFocus> getTaskFocusById(@Path("id") Long id);

    @GET("/taskFocus/list")
    Call<List<TaskFocus>> getAllTaskFocuses();

    @PUT("/taskFocus")
    Call<Boolean> updateTaskFocus(@Body TaskFocus taskFocus);

    @DELETE("/taskFocus/{id}")
    Call<Boolean> deleteTaskFocus(@Path("id") Long id);

    //=========================== 3. 专注记录 (FocusRecord) ===========================
    @POST("/focusRecord")
    Call<Boolean> createFocusRecord(@Body FocusRecord focusRecord);

    @GET("/focusRecord/{id}")
    Call<FocusRecord> getFocusRecordById(@Path("id") Long id);

    @GET("/focusRecord/list")
    Call<List<FocusRecord>> getAllFocusRecords();

    @PUT("/focusRecord")
    Call<Boolean> updateFocusRecord(@Body FocusRecord focusRecord);

    @DELETE("/focusRecord/{id}")
    Call<Boolean> deleteFocusRecord(@Path("id") Long id);

    //=========================== 4. 习惯追踪 (HabitTrack) ===========================
    @POST("/habitTrack")
    Call<Boolean> createHabitTrack(@Body HabitTrack habitTrack);

    @GET("/habitTrack/{id}")
    Call<HabitTrack> getHabitTrackById(@Path("id") Long id);

    @GET("/habitTrack/list")
    Call<List<HabitTrack>> getAllHabitTracks();

    @PUT("/habitTrack")
    Call<Boolean> updateHabitTrack(@Body HabitTrack habitTrack);

    @DELETE("/habitTrack/{id}")
    Call<Boolean> deleteHabitTrack(@Path("id") Long id);

    //=========================== 5. 习惯打卡 (HabitCheckin) ===========================
    @POST("/habitCheckin")
    Call<Boolean> createHabitCheckin(@Body HabitCheckin habitCheckin);

    @GET("/habitCheckin/{id}")
    Call<HabitCheckin> getHabitCheckinById(@Path("id") Long id);

    @GET("/habitCheckin/list")
    Call<List<HabitCheckin>> getAllHabitCheckins();

    @PUT("/habitCheckin")
    Call<Boolean> updateHabitCheckin(@Body HabitCheckin habitCheckin);

    @DELETE("/habitCheckin/{id}")
    Call<Boolean> deleteHabitCheckin(@Path("id") Long id);

    //=========================== 6. 成就徽章 (Achievement) ===========================
    @POST("/achievement")
    Call<Boolean> createAchievement(@Body Achievement achievement);

    @GET("/achievement/{id}")
    Call<Achievement> getAchievementById(@Path("id") Long id);

    @GET("/achievement/list")
    Call<List<Achievement>> getAllAchievements();

    @PUT("/achievement")
    Call<Boolean> updateAchievement(@Body Achievement achievement);

    @DELETE("/achievement/{id}")
    Call<Boolean> deleteAchievement(@Path("id") Long id);

    //=========================== 7. 用户成就关联 (UserAchieveRel) ===========================
    @POST("/userAchieveRel")
    Call<Boolean> createUserAchieveRel(@Body UserAchieveRel userAchieveRel);

    @GET("/userAchieveRel/{id}")
    Call<UserAchieveRel> getUserAchieveRelById(@Path("id") Long id);

    @GET("/userAchieveRel/list")
    Call<List<UserAchieveRel>> getAllUserAchieveRels();

    @PUT("/userAchieveRel")
    Call<Boolean> updateUserAchieveRel(@Body UserAchieveRel userAchieveRel);

    @DELETE("/userAchieveRel/{id}")
    Call<Boolean> deleteUserAchieveRel(@Path("id") Long id);

    //=========================== 8. 学习资源 (LearnResource) ===========================
    @POST("/learnResource")
    Call<Boolean> createLearnResource(@Body LearnResource learnResource);

    @GET("/learnResource/{id}")
    Call<LearnResource> getLearnResourceById(@Path("id") Long id);

    @GET("/learnResource/list")
    Call<List<LearnResource>> getAllLearnResources();

    @PUT("/learnResource")
    Call<Boolean> updateLearnResource(@Body LearnResource learnResource);

    @DELETE("/learnResource/{id}")
    Call<Boolean> deleteLearnResource(@Path("id") Long id);

    //=========================== 9. 资源分类 (ResourceCategory) ===========================
    @POST("/resourceCategory")
    Call<Boolean> createResourceCategory(@Body ResourceCategory resourceCategory);

    @GET("/resourceCategory/{id}")
    Call<ResourceCategory> getResourceCategoryById(@Path("id") Long id);

    @GET("/resourceCategory/list")
    Call<List<ResourceCategory>> getAllResourceCategories();

    @PUT("/resourceCategory")
    Call<Boolean> updateResourceCategory(@Body ResourceCategory resourceCategory);

    @DELETE("/resourceCategory/{id}")
    Call<Boolean> deleteResourceCategory(@Path("id") Long id);

    //=========================== 10. 校园好友 (CampusFriend) ===========================
    @POST("/campusFriend")
    Call<Boolean> createCampusFriend(@Body CampusFriend campusFriend);

    @GET("/campusFriend/{id}")
    Call<CampusFriend> getCampusFriendById(@Path("id") Long id);

    @GET("/campusFriend/list")
    Call<List<CampusFriend>> getAllCampusFriends();

    @PUT("/campusFriend")
    Call<Boolean> updateCampusFriend(@Body CampusFriend campusFriend);

    @DELETE("/campusFriend/{id}")
    Call<Boolean> deleteCampusFriend(@Path("id") Long id);

    //=========================== 11. 用户设置 (UserSetting) ===========================
    @POST("/userSetting")
    Call<Boolean> createUserSetting(@Body UserSetting userSetting);

    @GET("/userSetting/{id}")
    Call<UserSetting> getUserSettingById(@Path("id") Long id);

    @GET("/userSetting/list")
    Call<List<UserSetting>> getAllUserSettings();

    @PUT("/userSetting")
    Call<Boolean> updateUserSetting(@Body UserSetting userSetting);

    @DELETE("/userSetting/{id}")
    Call<Boolean> deleteUserSetting(@Path("id") Long id);

    //=========================== 12. 通知中心 (NotificationCenter) ===========================
    @POST("/notificationCenter")
    Call<Boolean> createNotificationCenter(@Body NotificationCenter notificationCenter);

    @GET("/notificationCenter/{id}")
    Call<NotificationCenter> getNotificationCenterById(@Path("id") Long id);

    @GET("/notificationCenter/list")
    Call<List<NotificationCenter>> getAllNotificationCenters();

    @PUT("/notificationCenter")
    Call<Boolean> updateNotificationCenter(@Body NotificationCenter notificationCenter);

    @DELETE("/notificationCenter/{id}")
    Call<Boolean> deleteNotificationCenter(@Path("id") Long id);
}