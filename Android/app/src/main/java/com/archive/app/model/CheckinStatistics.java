package com.archive.app.model;

import java.io.Serializable;

// 统计数据模型
public class CheckinStatistics implements Serializable {
    private int totalStudents;
    private int presentCount;
    private int lateCount;
    private int absentCount;

    // Getters and Setters...
    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
    public int getPresentCount() { return presentCount; }
    public void setPresentCount(int presentCount) { this.presentCount = presentCount; }
    public int getLateCount() { return lateCount; }
    public void setLateCount(int lateCount) { this.lateCount = lateCount; }
    public int getAbsentCount() { return absentCount; }
    public void setAbsentCount(int absentCount) { this.absentCount = absentCount; }
}