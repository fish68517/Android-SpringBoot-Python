package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class LearnResource {
    @SerializedName("learnResourceId")
    private Long learnResourceId;
    @SerializedName("resourceCategoryId")
    private Long resourceCategoryId;
    @SerializedName("resourceNameText")
    private String resourceNameText;
    @SerializedName("resourceUrlText")
    private String resourceUrlText;
    @SerializedName("resourceDescriptionText")
    private String resourceDescriptionText;
    @SerializedName("resourceTypeEnum")
    private String resourceTypeEnum;
    @SerializedName("resourceRecommendLevel")
    private Integer resourceRecommendLevel;
    @SerializedName("resourceCreateTimestamp")
    private String resourceCreateTimestamp;
    @SerializedName("resourceUpdateTimestamp")
    private String resourceUpdateTimestamp;
    @SerializedName("resourceClickCount")
    private Integer resourceClickCount;

    // Getters and Setters...

    public Long getLearnResourceId() {
        return learnResourceId;
    }

    public void setLearnResourceId(Long learnResourceId) {
        this.learnResourceId = learnResourceId;
    }

    public Long getResourceCategoryId() {
        return resourceCategoryId;
    }

    public void setResourceCategoryId(Long resourceCategoryId) {
        this.resourceCategoryId = resourceCategoryId;
    }

    public String getResourceNameText() {
        return resourceNameText;
    }

    public void setResourceNameText(String resourceNameText) {
        this.resourceNameText = resourceNameText;
    }

    public String getResourceUrlText() {
        return resourceUrlText;
    }

    public void setResourceUrlText(String resourceUrlText) {
        this.resourceUrlText = resourceUrlText;
    }

    public String getResourceDescriptionText() {
        return resourceDescriptionText;
    }

    public void setResourceDescriptionText(String resourceDescriptionText) {
        this.resourceDescriptionText = resourceDescriptionText;
    }

    public String getResourceTypeEnum() {
        return resourceTypeEnum;
    }

    public void setResourceTypeEnum(String resourceTypeEnum) {
        this.resourceTypeEnum = resourceTypeEnum;
    }

    public Integer getResourceRecommendLevel() {
        return resourceRecommendLevel;
    }

    public void setResourceRecommendLevel(Integer resourceRecommendLevel) {
        this.resourceRecommendLevel = resourceRecommendLevel;
    }

    public String getResourceCreateTimestamp() {
        return resourceCreateTimestamp;
    }

    public void setResourceCreateTimestamp(String resourceCreateTimestamp) {
        this.resourceCreateTimestamp = resourceCreateTimestamp;
    }

    public String getResourceUpdateTimestamp() {
        return resourceUpdateTimestamp;
    }

    public void setResourceUpdateTimestamp(String resourceUpdateTimestamp) {
        this.resourceUpdateTimestamp = resourceUpdateTimestamp;
    }

    public Integer getResourceClickCount() {
        return resourceClickCount;
    }

    public void setResourceClickCount(Integer resourceClickCount) {
        this.resourceClickCount = resourceClickCount;
    }
}