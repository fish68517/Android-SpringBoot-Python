package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class TaskFocus {
    @SerializedName("taskFocusId")
    private Long taskFocusId;
    @SerializedName("campusUserId")
    private Long campusUserId;
    @SerializedName("taskTitleText")
    private String taskTitleText;
    @SerializedName("taskDescriptionText")
    private String taskDescriptionText;
    @SerializedName("taskDeadlineTimestamp")
    private String taskDeadlineTimestamp;
    @SerializedName("taskStatusEnum")
    private String taskStatusEnum;
    @SerializedName("taskCreateTimestamp")
    private String taskCreateTimestamp;
    @SerializedName("taskPriorityCode")
    private Integer taskPriorityCode;
    @SerializedName("taskReminderTimestamp")
    private String taskReminderTimestamp;
    @SerializedName("taskCategoryCode")
    private String taskCategoryCode;
    @SerializedName("taskFocusDurationMins")
    private Integer taskFocusDurationMins;
    @SerializedName("taskBreakDurationMins")
    private Integer taskBreakDurationMins;
    @SerializedName("taskAppBlockFlag")
    private Boolean taskAppBlockFlag;
    @SerializedName("taskActualMinutes")
    private Integer taskActualMinutes;

    // Getters and Setters...


    public Long getTaskFocusId() {
        return taskFocusId;
    }

    public void setTaskFocusId(Long taskFocusId) {
        this.taskFocusId = taskFocusId;
    }

    public Long getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(Long campusUserId) {
        this.campusUserId = campusUserId;
    }

    public String getTaskTitleText() {
        return taskTitleText;
    }

    public void setTaskTitleText(String taskTitleText) {
        this.taskTitleText = taskTitleText;
    }

    public String getTaskDescriptionText() {
        return taskDescriptionText;
    }

    public void setTaskDescriptionText(String taskDescriptionText) {
        this.taskDescriptionText = taskDescriptionText;
    }

    public String getTaskDeadlineTimestamp() {
        return taskDeadlineTimestamp;
    }

    public void setTaskDeadlineTimestamp(String taskDeadlineTimestamp) {
        this.taskDeadlineTimestamp = taskDeadlineTimestamp;
    }

    public String getTaskStatusEnum() {
        return taskStatusEnum;
    }

    public void setTaskStatusEnum(String taskStatusEnum) {
        this.taskStatusEnum = taskStatusEnum;
    }

    public String getTaskCreateTimestamp() {
        return taskCreateTimestamp;
    }

    public void setTaskCreateTimestamp(String taskCreateTimestamp) {
        this.taskCreateTimestamp = taskCreateTimestamp;
    }

    public Integer getTaskPriorityCode() {
        return taskPriorityCode;
    }

    public void setTaskPriorityCode(Integer taskPriorityCode) {
        this.taskPriorityCode = taskPriorityCode;
    }

    public String getTaskReminderTimestamp() {
        return taskReminderTimestamp;
    }

    public void setTaskReminderTimestamp(String taskReminderTimestamp) {
        this.taskReminderTimestamp = taskReminderTimestamp;
    }

    public String getTaskCategoryCode() {
        return taskCategoryCode;
    }

    public void setTaskCategoryCode(String taskCategoryCode) {
        this.taskCategoryCode = taskCategoryCode;
    }

    public Integer getTaskFocusDurationMins() {
        return taskFocusDurationMins;
    }

    public void setTaskFocusDurationMins(Integer taskFocusDurationMins) {
        this.taskFocusDurationMins = taskFocusDurationMins;
    }

    public Integer getTaskBreakDurationMins() {
        return taskBreakDurationMins;
    }

    public void setTaskBreakDurationMins(Integer taskBreakDurationMins) {
        this.taskBreakDurationMins = taskBreakDurationMins;
    }

    public Boolean getTaskAppBlockFlag() {
        return taskAppBlockFlag;
    }

    public void setTaskAppBlockFlag(Boolean taskAppBlockFlag) {
        this.taskAppBlockFlag = taskAppBlockFlag;
    }

    public Integer getTaskActualMinutes() {
        return taskActualMinutes;
    }

    public void setTaskActualMinutes(Integer taskActualMinutes) {
        this.taskActualMinutes = taskActualMinutes;
    }
}