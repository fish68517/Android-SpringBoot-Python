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
}