package com.graduation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Use LocalDateTime to match the entity

@Data
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String courseName;
    private String teacherName;
    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;
    private String status;
    private String classroomPolygon;

    private Long courseId;


    // 添加包含 courseId 的构造函数（8个参数）
    public NotificationDTO(Long id, String courseName, String teacherName, LocalDateTime creationTime, LocalDateTime expirationTime, String status, String classroomPolygon, Long courseId) {
        this.id = id;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.status = status;
        this.classroomPolygon = classroomPolygon;
        this.courseId = courseId;
    }


      // 添加包含 courseId 的构造函数（8个参数）
    public NotificationDTO(Long id, String courseName, String teacherName, LocalDateTime creationTime, LocalDateTime expirationTime, String status, String classroomPolygon, int courseId) {
        this.id = id;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.status = status;
        this.classroomPolygon = classroomPolygon;
        this.courseId = Long.valueOf(courseId);
    }




    // Cannot instantiate class 'com.graduation.dto.NotificationDTO' (it has no constructor with signature [java.lang.Long, java.lang.String, java.lang.String, java.time.LocalDateTime, java.time.LocalDateTime, java.lang.String], and not every argument has an alias)
    public NotificationDTO(Long id, String courseName, String teacherName, LocalDateTime creationTime, LocalDateTime expirationTime, String status, String classroomPolygon) {
        this.id = id;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.status = status;
        this.classroomPolygon = classroomPolygon;
    }

    public int getCourseId() {
        return Math.toIntExact(courseId);
    }

    public void setCourseId(int courseId) {
        this.courseId = (long) courseId;
    }

    public String getClassroomPolygon() {
        return classroomPolygon;
    }

    public void setClassroomPolygon(String classroomPolygon) {
        this.classroomPolygon = classroomPolygon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}