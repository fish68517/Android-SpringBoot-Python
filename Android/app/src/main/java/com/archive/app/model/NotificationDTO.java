package com.archive.app.model;

import java.io.Serializable;
import java.util.Date;

// 用于从后端接收签到通知的数据传输对象 (DTO)
public class NotificationDTO implements Serializable {
    private Long id;
    private String courseName;
    private String teacherName;
    private Date creationTime;
    private Date expirationTime;
    private String status; // "ACTIVE", "EXPIRED"

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public Date getCreationTime() { return creationTime; }
    public void setCreationTime(Date creationTime) { this.creationTime = creationTime; }
    public Date getExpirationTime() { return expirationTime; }
    public void setExpirationTime(Date expirationTime) { this.expirationTime = expirationTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}