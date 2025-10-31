package com.archive.app.model;

import java.io.Serializable;

// 课程模型
public class Course implements Serializable {
    private Long id;
    private String courseName;
    private String courseCode;
    private Long teacherId; // 教师ID
    // 可以添加教室、时间等更多信息

    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}