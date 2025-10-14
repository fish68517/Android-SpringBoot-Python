package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class UserSetting {
    @SerializedName("userSettingId")
    private Long userSettingId;
    @SerializedName("campusUserId")
    private Long campusUserId;
    @SerializedName("settingThemeEnum")
    private String settingThemeEnum;
    @SerializedName("settingDefaultFocusMins")
    private Integer settingDefaultFocusMins;
    @SerializedName("settingDefaultBreakMins")
    private Integer settingDefaultBreakMins;
    @SerializedName("settingNotificationFlag")
    private Boolean settingNotificationFlag;
    @SerializedName("settingPrivacyLevel")
    private Integer settingPrivacyLevel;
    @SerializedName("settingDailyReminderTime")
    private String settingDailyReminderTime;
    @SerializedName("settingWeekStartEnum")
    private String settingWeekStartEnum;
    @SerializedName("settingCreateTimestamp")
    private String settingCreateTimestamp;
    @SerializedName("settingUpdateTimestamp")
    private String settingUpdateTimestamp;

    // Getters and Setters...
}