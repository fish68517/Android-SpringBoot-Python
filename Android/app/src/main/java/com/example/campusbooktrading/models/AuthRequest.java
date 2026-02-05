package com.example.campusbooktrading.models;

import com.google.gson.annotations.SerializedName;

/**
 * 认证请求数据模型
 */
public class AuthRequest {
    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("username")
    public String username;

    @SerializedName("student_id")
    public String studentId;

    public AuthRequest() {
    }

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthRequest(String username, String email, String password, String studentId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.studentId = studentId;
    }
}
