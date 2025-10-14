package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class HabitTrack {
    @SerializedName("habitTrackId")
    private Long habitTrackId;
    @SerializedName("campusUserId")
    private Long campusUserId;
    @SerializedName("habitNameText")
    private String habitNameText;
    @SerializedName("habitFrequencyEnum")
    private String habitFrequencyEnum;
    @SerializedName("habitReminderTime")
    private String habitReminderTime;
    @SerializedName("habitStatusEnum")
    private String habitStatusEnum;
    @SerializedName("habitCreateTimestamp")
    private String habitCreateTimestamp;
    @SerializedName("habitStreakCount")
    private Integer habitStreakCount;
    @SerializedName("habitTotalCount")
    private Integer habitTotalCount;
    @SerializedName("habitGoalCount")
    private Integer habitGoalCount;
    @SerializedName("habitColorHex")
    private String habitColorHex;
    @SerializedName("habitIconCode")
    private String habitIconCode;

    // Getters and Setters...


    public Long getHabitTrackId() {
        return habitTrackId;
    }

    public void setHabitTrackId(Long habitTrackId) {
        this.habitTrackId = habitTrackId;
    }

    public Long getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(Long campusUserId) {
        this.campusUserId = campusUserId;
    }

    public String getHabitNameText() {
        return habitNameText;
    }

    public void setHabitNameText(String habitNameText) {
        this.habitNameText = habitNameText;
    }

    public String getHabitFrequencyEnum() {
        return habitFrequencyEnum;
    }

    public void setHabitFrequencyEnum(String habitFrequencyEnum) {
        this.habitFrequencyEnum = habitFrequencyEnum;
    }

    public String getHabitReminderTime() {
        return habitReminderTime;
    }

    public void setHabitReminderTime(String habitReminderTime) {
        this.habitReminderTime = habitReminderTime;
    }

    public String getHabitStatusEnum() {
        return habitStatusEnum;
    }

    public void setHabitStatusEnum(String habitStatusEnum) {
        this.habitStatusEnum = habitStatusEnum;
    }

    public String getHabitCreateTimestamp() {
        return habitCreateTimestamp;
    }

    public void setHabitCreateTimestamp(String habitCreateTimestamp) {
        this.habitCreateTimestamp = habitCreateTimestamp;
    }

    public Integer getHabitStreakCount() {
        return habitStreakCount;
    }

    public void setHabitStreakCount(Integer habitStreakCount) {
        this.habitStreakCount = habitStreakCount;
    }

    public Integer getHabitTotalCount() {
        return habitTotalCount;
    }

    public void setHabitTotalCount(Integer habitTotalCount) {
        this.habitTotalCount = habitTotalCount;
    }

    public Integer getHabitGoalCount() {
        return habitGoalCount;
    }

    public void setHabitGoalCount(Integer habitGoalCount) {
        this.habitGoalCount = habitGoalCount;
    }

    public String getHabitColorHex() {
        return habitColorHex;
    }

    public void setHabitColorHex(String habitColorHex) {
        this.habitColorHex = habitColorHex;
    }

    public String getHabitIconCode() {
        return habitIconCode;
    }

    public void setHabitIconCode(String habitIconCode) {
        this.habitIconCode = habitIconCode;
    }
}