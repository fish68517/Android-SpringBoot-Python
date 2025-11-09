package com.graduation.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CheckinStatistics implements Serializable {
    private int totalStudents; // 假设的总人数
    private int presentCount;  // "成功"
    private int lateCount;     // "迟到"
    private int absentCount;   // 缺勤 (总人数 - 成功 - 迟到)

    public CheckinStatistics(int totalStudents, int presentCount, int lateCount) {
        this.totalStudents = totalStudents;
        this.presentCount = presentCount;
        this.lateCount = lateCount;
        this.absentCount = totalStudents - presentCount - lateCount;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }
}