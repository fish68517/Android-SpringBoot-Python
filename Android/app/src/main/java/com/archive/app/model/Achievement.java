package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class Achievement {
    @SerializedName("achievementId")
    private Long achievementId;
    @SerializedName("achieveNameText")
    private String achieveNameText;
    @SerializedName("achieveDescriptionText")
    private String achieveDescriptionText;
    @SerializedName("achieveIconUrl")
    private String achieveIconUrl;
    @SerializedName("achieveRuleText")
    private String achieveRuleText;
    @SerializedName("achieveTypeEnum")
    private String achieveTypeEnum;
    @SerializedName("achieveCreateTimestamp")
    private String achieveCreateTimestamp;
    @SerializedName("achieveActiveFlag")
    private Boolean achieveActiveFlag;

    // Getters and Setters...


    public Long getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Long achievementId) {
        this.achievementId = achievementId;
    }

    public String getAchieveNameText() {
        return achieveNameText;
    }

    public void setAchieveNameText(String achieveNameText) {
        this.achieveNameText = achieveNameText;
    }

    public String getAchieveDescriptionText() {
        return achieveDescriptionText;
    }

    public void setAchieveDescriptionText(String achieveDescriptionText) {
        this.achieveDescriptionText = achieveDescriptionText;
    }

    public String getAchieveIconUrl() {
        return achieveIconUrl;
    }

    public void setAchieveIconUrl(String achieveIconUrl) {
        this.achieveIconUrl = achieveIconUrl;
    }

    public String getAchieveRuleText() {
        return achieveRuleText;
    }

    public void setAchieveRuleText(String achieveRuleText) {
        this.achieveRuleText = achieveRuleText;
    }

    public String getAchieveTypeEnum() {
        return achieveTypeEnum;
    }

    public void setAchieveTypeEnum(String achieveTypeEnum) {
        this.achieveTypeEnum = achieveTypeEnum;
    }

    public String getAchieveCreateTimestamp() {
        return achieveCreateTimestamp;
    }

    public void setAchieveCreateTimestamp(String achieveCreateTimestamp) {
        this.achieveCreateTimestamp = achieveCreateTimestamp;
    }

    public Boolean getAchieveActiveFlag() {
        return achieveActiveFlag;
    }

    public void setAchieveActiveFlag(Boolean achieveActiveFlag) {
        this.achieveActiveFlag = achieveActiveFlag;
    }
}