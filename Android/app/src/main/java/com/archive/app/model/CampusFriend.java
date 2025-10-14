package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class CampusFriend {
    @SerializedName("campusFriendId")
    private Long campusFriendId;
    @SerializedName("campusUserId")
    private Long campusUserId;
    @SerializedName("friendUserId")
    private Long friendUserId;
    @SerializedName("friendStatusEnum")
    private String friendStatusEnum;
    @SerializedName("friendCreateTimestamp")
    private String friendCreateTimestamp;
    @SerializedName("friendUpdateTimestamp")
    private String friendUpdateTimestamp;
    @SerializedName("friendVisibilityFlag")
    private Boolean friendVisibilityFlag;

    // Getters and Setters...

    public Long getCampusFriendId() {
        return campusFriendId;
    }

    public void setCampusFriendId(Long campusFriendId) {
        this.campusFriendId = campusFriendId;
    }

    public Long getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(Long campusUserId) {
        this.campusUserId = campusUserId;
    }

    public Long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Long friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendStatusEnum() {
        return friendStatusEnum;
    }

    public void setFriendStatusEnum(String friendStatusEnum) {
        this.friendStatusEnum = friendStatusEnum;
    }

    public String getFriendCreateTimestamp() {
        return friendCreateTimestamp;
    }

    public void setFriendCreateTimestamp(String friendCreateTimestamp) {
        this.friendCreateTimestamp = friendCreateTimestamp;
    }

    public String getFriendUpdateTimestamp() {
        return friendUpdateTimestamp;
    }

    public void setFriendUpdateTimestamp(String friendUpdateTimestamp) {
        this.friendUpdateTimestamp = friendUpdateTimestamp;
    }

    public Boolean getFriendVisibilityFlag() {
        return friendVisibilityFlag;
    }

    public void setFriendVisibilityFlag(Boolean friendVisibilityFlag) {
        this.friendVisibilityFlag = friendVisibilityFlag;
    }
}