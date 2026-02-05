package com.example.campusbooktrading.models;

import com.google.gson.annotations.SerializedName;

/**
 * 认证响应数据模型
 */
public class AuthResponse {
    @SerializedName("message")
    public String message;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("username")
    public String username;

    @SerializedName("email")
    public String email;

    @SerializedName("student_id")
    public String studentId;

    @SerializedName("token")
    public String token;

    @SerializedName("error")
    public String error;

    public AuthResponse() {
    }

    public boolean isSuccess() {
        return error == null || error.isEmpty();
    }
}
