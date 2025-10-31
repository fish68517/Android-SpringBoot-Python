package com.archive.app.model;

import java.io.Serializable;
import java.util.Date;

// 签到记录模型
public class CheckinRecord implements Serializable {
    private Long id;
    private Long userId; // 学生ID
    private Long courseId; // 课程ID
    private Date checkinTime;
    private double latitude;
    private double longitude;
    private String status; // 例如 "成功", "迟到", "缺勤"

    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public Date getCheckinTime() { return checkinTime; }
    public void setCheckinTime(Date checkinTime) { this.checkinTime = checkinTime; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}