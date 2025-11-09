package com.graduation.dto;

public class TeacherDto {
    private Long id;
    private String username; // 假设 CampusUser 有 username 字段
    // ... 其他您希望暴露的教师信息


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
