package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class UserAchieveRel {
    @SerializedName("userAchieveId")
    private Long userAchieveId;
    @SerializedName("campusUserId")
    private Long campusUserId;
    @SerializedName("achievementId")
    private Long achievementId;
    @SerializedName("achieveTimestamp")
    private String achieveTimestamp;
    @SerializedName("achieveConditionText")
    private String achieveConditionText;

    // Getters and Setters...


    public Long getUserAchieveId() {
        return userAchieveId;
    }

    public void setUserAchieveId(Long userAchieveId) {
        this.userAchieveId = userAchieveId;
    }

    public Long getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(Long campusUserId) {
        this.campusUserId = campusUserId;
    }

    public Long getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Long achievementId) {
        this.achievementId = achievementId;
    }

    public String getAchieveTimestamp() {
        return achieveTimestamp;
    }

    public void setAchieveTimestamp(String achieveTimestamp) {
        this.achieveTimestamp = achieveTimestamp;
    }

    public String getAchieveConditionText() {
        return achieveConditionText;
    }

    public void setAchieveConditionText(String achieveConditionText) {
        this.achieveConditionText = achieveConditionText;
    }
}