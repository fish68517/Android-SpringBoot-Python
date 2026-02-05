package com.example.campusbooktrading.models;

import com.google.gson.annotations.SerializedName;

/**
 * 用户数据模型
 */
public class User {
    @SerializedName("id")
    public int id;

    @SerializedName("username")
    public String username;

    @SerializedName("email")
    public String email;

    @SerializedName("student_id")
    public String studentId;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("active_listings")
    public int activeListings;

    @SerializedName("sold_listings")
    public int soldListings;

    @SerializedName("purchases")
    public int purchases;

    @SerializedName("sales")
    public int sales;

    public User() {
    }

    public User(String username, String email, String studentId) {
        this.username = username;
        this.email = email;
        this.studentId = studentId;
    }
}
