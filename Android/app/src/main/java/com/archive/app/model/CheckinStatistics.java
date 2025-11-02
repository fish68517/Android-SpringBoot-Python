package com.archive.app.model;

public class CheckinStatistics {
    private int successCount;
    private int lateCount;
    private int totalEnrolledStudents; // 课程总注册人数

    // 缺勤人数可以在后端计算好，也可以在前端计算
    public int getAbsentCount() {
        return totalEnrolledStudents - (successCount + lateCount);
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    public void setTotalEnrolledStudents(int totalEnrolledStudents) {
        this.totalEnrolledStudents = totalEnrolledStudents;
    }
}