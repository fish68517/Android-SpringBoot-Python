package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class HabitCheckin {
    @SerializedName("habitCheckinId")
    private Long habitCheckinId;
    @SerializedName("habitTrackId")
    private Long habitTrackId;
    @SerializedName("checkinDate")
    private String checkinDate;
    @SerializedName("checkinTimestamp")
    private String checkinTimestamp;
    @SerializedName("checkinStatusEnum")
    private String checkinStatusEnum;
    @SerializedName("checkinNoteText")
    private String checkinNoteText;
    @SerializedName("checkinStreakCount")
    private Integer checkinStreakCount;

    // Getters and Setters...


    public Long getHabitCheckinId() {
        return habitCheckinId;
    }

    public void setHabitCheckinId(Long habitCheckinId) {
        this.habitCheckinId = habitCheckinId;
    }

    public Long getHabitTrackId() {
        return habitTrackId;
    }

    public void setHabitTrackId(Long habitTrackId) {
        this.habitTrackId = habitTrackId;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckinTimestamp() {
        return checkinTimestamp;
    }

    public void setCheckinTimestamp(String checkinTimestamp) {
        this.checkinTimestamp = checkinTimestamp;
    }

    public String getCheckinStatusEnum() {
        return checkinStatusEnum;
    }

    public void setCheckinStatusEnum(String checkinStatusEnum) {
        this.checkinStatusEnum = checkinStatusEnum;
    }

    public String getCheckinNoteText() {
        return checkinNoteText;
    }

    public void setCheckinNoteText(String checkinNoteText) {
        this.checkinNoteText = checkinNoteText;
    }

    public Integer getCheckinStreakCount() {
        return checkinStreakCount;
    }

    public void setCheckinStreakCount(Integer checkinStreakCount) {
        this.checkinStreakCount = checkinStreakCount;
    }
}