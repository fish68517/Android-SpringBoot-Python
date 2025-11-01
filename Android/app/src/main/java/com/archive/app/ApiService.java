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
import retrofit2.http.Query;

/**
 * 定义所有与后端Spring Boot的API接口 (完整版)
 */
public interface ApiService {

    //=========================== 1. 校园用户 (CampusUser) ===========================
    @POST("/campusUser/login")
    Call<User> login(@Body User user);

    @POST("/campusUser")
    Call<Boolean> register(@Body User user);

    @GET("/campusUser/{id}")
    Call<User> getCampusUserById(@Path("id") Long id);

    @GET("/campusUser/list")
    Call<List<User>> getAllCampusUsers();

    @PUT("/campusUser")
    Call<Boolean> updateCampusUser(@Body User user);

    @DELETE("/campusUser/{id}")
    Call<Boolean> deleteCampusUser(@Path("id") Long id);

    //=========================== 2. 学生 - 签到功能 ===========================

    /**
     * (由 MapFragment 调用) 保存一次签到记录
     */
    @POST("/api/checkin")
    Call<CheckinRecord> saveCheckinRecord(@Body CheckinRecord record);

    /**
     * (由 CheckinHistoryFragment 调用) 获取某个学生的所有签到记录
     */
    @GET("/api/checkin/history/user/{userId}")
    Call<List<CheckinRecord>> getCheckinHistory(@Path("userId") Long userId);

    /**
     * (由 CheckinNotificationFragment 调用) 获取激活的签到通知
     */
    @GET("/api/checkin/notifications/{studentId}")
    Call<List<Object>> getCheckinNotifications(@Path("studentId") Long studentId); // 假设返回一个通知列表

    //=========================== 3. 教师 - 课程管理 ===========================

    @POST("/api/courses")
    Call<Course> createCourse(@Body Course course);

    @GET("/api/courses/teacher/{teacherId}")
    Call<List<Course>> getCoursesByTeacher(@Path("teacherId") Long teacherId);

    @PUT("/api/courses/{courseId}")
    Call<Course> updateCourse(@Path("courseId") Long courseId, @Body Course course);

    @DELETE("/api/courses/{courseId}")
    Call<Void> deleteCourse(@Path("courseId") Long courseId);

    //=========================== 4. 教师 - 签到管理 ===========================

    /**
     * (由 StartCheckinFragment 调用) 教师发起签到
     * @param courseId 课程ID
     * @param durationMinutes 签到有效时间（例如 10 分钟）
     */
    @POST("/api/checkin/start")
    Call<Void> startCheckin(@Query("courseId") Long courseId, @Query("durationMinutes") int durationMinutes);

    //=========================== 5. 教师 - 数据统计 ===========================

    @GET("/api/statistics/daily")
    Call<CheckinStatistics> getDailyStatistics(@Query("courseId") Long courseId, @Query("date") String date);

    @GET("/api/statistics/weekly")
    Call<CheckinStatistics> getWeeklyStatistics(@Query("courseId") Long courseId, @Query("week") String week);

    @GET("/api/statistics/monthly")
    Call<CheckinStatistics> getMonthlyStatistics(@Query("courseId") Long courseId, @Query("month") String month);


    // ApiService.java

// ... 其他接口

// =========================== 5. 学生 - 签到通知 ===========================

    /**
     * (由 CheckinNotificationFragment 调用) 获取对学生可见的签到通知列表
     * @param studentId 当前学生的ID
     * @return 一个包含课程和教师信息的通知列表
     */
    @GET("/api/notifications/student/{studentId}")
    Call<List<NotificationDTO>> getNotificationsForStudent(@Path("studentId") Long studentId);
}