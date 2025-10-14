package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class NotificationCenter {
    @SerializedName("notificationId")
    private Long notificationId;
    @SerializedName("campusUserId")
    private Long campusUserId;
    @SerializedName("notificationTypeEnum")
    private String notificationTypeEnum;
    @SerializedName("notificationTitleText")
    private String notificationTitleText;
    @SerializedName("notificationContentText")
    private String notificationContentText;
    @SerializedName("notificationIsReadFlag")
    private Boolean notificationIsReadFlag;
    @SerializedName("notificationCreateTimestamp")
    private String notificationCreateTimestamp;
    @SerializedName("notificationExpireTimestamp")
    private String notificationExpireTimestamp;
    @SerializedName("notificationActionUrl")
    private String notificationActionUrl;
    @SerializedName("notificationPriorityCode")
    private Integer notificationPriorityCode;

    // Getters and Setters...


    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(Long campusUserId) {
        this.campusUserId = campusUserId;
    }

    public String getNotificationTypeEnum() {
        return notificationTypeEnum;
    }

    public void setNotificationTypeEnum(String notificationTypeEnum) {
        this.notificationTypeEnum = notificationTypeEnum;
    }

    public String getNotificationTitleText() {
        return notificationTitleText;
    }

    public void setNotificationTitleText(String notificationTitleText) {
        this.notificationTitleText = notificationTitleText;
    }

    public String getNotificationContentText() {
        return notificationContentText;
    }

    public void setNotificationContentText(String notificationContentText) {
        this.notificationContentText = notificationContentText;
    }

    public Boolean getNotificationIsReadFlag() {
        return notificationIsReadFlag;
    }

    public void setNotificationIsReadFlag(Boolean notificationIsReadFlag) {
        this.notificationIsReadFlag = notificationIsReadFlag;
    }

    public String getNotificationCreateTimestamp() {
        return notificationCreateTimestamp;
    }

    public void setNotificationCreateTimestamp(String notificationCreateTimestamp) {
        this.notificationCreateTimestamp = notificationCreateTimestamp;
    }

    public String getNotificationExpireTimestamp() {
        return notificationExpireTimestamp;
    }

    public void setNotificationExpireTimestamp(String notificationExpireTimestamp) {
        this.notificationExpireTimestamp = notificationExpireTimestamp;
    }

    public String getNotificationActionUrl() {
        return notificationActionUrl;
    }

    public void setNotificationActionUrl(String notificationActionUrl) {
        this.notificationActionUrl = notificationActionUrl;
    }

    public Integer getNotificationPriorityCode() {
        return notificationPriorityCode;
    }

    public void setNotificationPriorityCode(Integer notificationPriorityCode) {
        this.notificationPriorityCode = notificationPriorityCode;
    }
}