package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class CampusUser {

    @SerializedName("campusUserId")
    private Long campusUserId;

    @SerializedName("campusNickname")
    private String campusNickname;

    @SerializedName("campusAvatarUrl")
    private String campusAvatarUrl;

    @SerializedName("campusSchoolId")
    private String campusSchoolId;

    @SerializedName("campusEmailAddr")
    private String campusEmailAddr;

    // 注意：注册时需要提交密码，但后端通常不会返回密码字段
    @SerializedName("password")
    private String password;

    @SerializedName("campusStatusFlag")
    private Integer campusStatusFlag;

    @SerializedName("campusCreateTimestamp")
    private String campusCreateTimestamp; // 在App端通常用String接收日期时间

    @SerializedName("campusLevelCode")
    private Integer campusLevelCode;

    @SerializedName("campusBadgeCount")
    private Integer campusBadgeCount;

    @SerializedName("campusLastLoginTs")
    private String campusLastLoginTs;

    @SerializedName("campusUserType")
    private String campusUserType;

    // Getter and Setter
    // 您可以使用Android Studio的快捷键 (Alt+Insert) 快速生成所有字段的Getter和Setter方法
    // ...

    public Long getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(Long campusUserId) {
        this.campusUserId = campusUserId;
    }

    public String getCampusNickname() {
        return campusNickname;
    }

    public void setCampusNickname(String campusNickname) {
        this.campusNickname = campusNickname;
    }

    public String getCampusAvatarUrl() {
        return campusAvatarUrl;
    }

    public void setCampusAvatarUrl(String campusAvatarUrl) {
        this.campusAvatarUrl = campusAvatarUrl;
    }

    public String getCampusSchoolId() {
        return campusSchoolId;
    }

    public void setCampusSchoolId(String campusSchoolId) {
        this.campusSchoolId = campusSchoolId;
    }

    public String getCampusEmailAddr() {
        return campusEmailAddr;
    }

    public void setCampusEmailAddr(String campusEmailAddr) {
        this.campusEmailAddr = campusEmailAddr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCampusStatusFlag() {
        return campusStatusFlag;
    }

    public void setCampusStatusFlag(Integer campusStatusFlag) {
        this.campusStatusFlag = campusStatusFlag;
    }

    public String getCampusCreateTimestamp() {
        return campusCreateTimestamp;
    }

    public void setCampusCreateTimestamp(String campusCreateTimestamp) {
        this.campusCreateTimestamp = campusCreateTimestamp;
    }

    public Integer getCampusLevelCode() {
        return campusLevelCode;
    }

    public void setCampusLevelCode(Integer campusLevelCode) {
        this.campusLevelCode = campusLevelCode;
    }

    public Integer getCampusBadgeCount() {
        return campusBadgeCount;
    }

    public void setCampusBadgeCount(Integer campusBadgeCount) {
        this.campusBadgeCount = campusBadgeCount;
    }

    public String getCampusLastLoginTs() {
        return campusLastLoginTs;
    }

    public void setCampusLastLoginTs(String campusLastLoginTs) {
        this.campusLastLoginTs = campusLastLoginTs;
    }

    public String getCampusUserType() {
        return campusUserType;
    }

    public void setCampusUserType(String campusUserType) {
        this.campusUserType = campusUserType;
    }
}