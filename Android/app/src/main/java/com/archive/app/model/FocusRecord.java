package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class FocusRecord {
    @SerializedName("focusRecordId")
    private Long focusRecordId;
    @SerializedName("campusUserId")
    private Long campusUserId;
    @SerializedName("taskFocusId")
    private Long taskFocusId;
    @SerializedName("focusStartTimestamp")
    private String focusStartTimestamp;
    @SerializedName("focusEndTimestamp")
    private String focusEndTimestamp;
    @SerializedName("focusDurationSeconds")
    private Integer focusDurationSeconds;
    @SerializedName("focusTypeEnum")
    private String focusTypeEnum;
    @SerializedName("focusStatusEnum")
    private String focusStatusEnum;
    @SerializedName("focusInterruptionCount")
    private Integer focusInterruptionCount;
    @SerializedName("focusPauseCount")
    private Integer focusPauseCount;
    @SerializedName("focusNoteText")
    private String focusNoteText;
    @SerializedName("focusAppBlockFlag")
    private Boolean focusAppBlockFlag;

    // Getters and Setters...


    public Long getFocusRecordId() {
        return focusRecordId;
    }

    public void setFocusRecordId(Long focusRecordId) {
        this.focusRecordId = focusRecordId;
    }

    public Long getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(Long campusUserId) {
        this.campusUserId = campusUserId;
    }

    public Long getTaskFocusId() {
        return taskFocusId;
    }

    public void setTaskFocusId(Long taskFocusId) {
        this.taskFocusId = taskFocusId;
    }

    public String getFocusStartTimestamp() {
        return focusStartTimestamp;
    }

    public void setFocusStartTimestamp(String focusStartTimestamp) {
        this.focusStartTimestamp = focusStartTimestamp;
    }

    public String getFocusEndTimestamp() {
        return focusEndTimestamp;
    }

    public void setFocusEndTimestamp(String focusEndTimestamp) {
        this.focusEndTimestamp = focusEndTimestamp;
    }

    public Integer getFocusDurationSeconds() {
        return focusDurationSeconds;
    }

    public void setFocusDurationSeconds(Integer focusDurationSeconds) {
        this.focusDurationSeconds = focusDurationSeconds;
    }

    public String getFocusTypeEnum() {
        return focusTypeEnum;
    }

    public void setFocusTypeEnum(String focusTypeEnum) {
        this.focusTypeEnum = focusTypeEnum;
    }

    public String getFocusStatusEnum() {
        return focusStatusEnum;
    }

    public void setFocusStatusEnum(String focusStatusEnum) {
        this.focusStatusEnum = focusStatusEnum;
    }

    public Integer getFocusInterruptionCount() {
        return focusInterruptionCount;
    }

    public void setFocusInterruptionCount(Integer focusInterruptionCount) {
        this.focusInterruptionCount = focusInterruptionCount;
    }

    public Integer getFocusPauseCount() {
        return focusPauseCount;
    }

    public void setFocusPauseCount(Integer focusPauseCount) {
        this.focusPauseCount = focusPauseCount;
    }

    public String getFocusNoteText() {
        return focusNoteText;
    }

    public void setFocusNoteText(String focusNoteText) {
        this.focusNoteText = focusNoteText;
    }

    public Boolean getFocusAppBlockFlag() {
        return focusAppBlockFlag;
    }

    public void setFocusAppBlockFlag(Boolean focusAppBlockFlag) {
        this.focusAppBlockFlag = focusAppBlockFlag;
    }
}