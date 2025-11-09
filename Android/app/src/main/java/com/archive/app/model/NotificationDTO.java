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

    private Long courseId;



    private String classroomPolygon; // 格式: "lat,lon;lat,lon;..."


    private double thresholdMeters;


    public String getClassroomPolygon() {
        return classroomPolygon;
    }

    public void setClassroomPolygon(String classroomPolygon) {
        this.classroomPolygon = classroomPolygon;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getLocationPolygon() {
        return classroomPolygon;
    }

    public void setLocationPolygon(String locationPolygon) {
        this.classroomPolygon = locationPolygon;
    }

    public double getThresholdMeters() {
        return thresholdMeters;
    }

    public void setThresholdMeters(double thresholdMeters) {
        this.thresholdMeters = thresholdMeters;
    }

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