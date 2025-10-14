package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

public class ResourceCategory {
    @SerializedName("resourceCategoryId")
    private Long resourceCategoryId;
    @SerializedName("categoryNameText")
    private String categoryNameText;
    @SerializedName("categoryDescriptionText")
    private String categoryDescriptionText;
    @SerializedName("categoryOrderNumber")
    private Integer categoryOrderNumber;
    @SerializedName("categoryIconCode")
    private String categoryIconCode;
    @SerializedName("categoryCreateTimestamp")
    private String categoryCreateTimestamp;

    // Getters and Setters...


    public Long getResourceCategoryId() {
        return resourceCategoryId;
    }

    public void setResourceCategoryId(Long resourceCategoryId) {
        this.resourceCategoryId = resourceCategoryId;
    }

    public String getCategoryNameText() {
        return categoryNameText;
    }

    public void setCategoryNameText(String categoryNameText) {
        this.categoryNameText = categoryNameText;
    }

    public String getCategoryDescriptionText() {
        return categoryDescriptionText;
    }

    public void setCategoryDescriptionText(String categoryDescriptionText) {
        this.categoryDescriptionText = categoryDescriptionText;
    }

    public Integer getCategoryOrderNumber() {
        return categoryOrderNumber;
    }

    public void setCategoryOrderNumber(Integer categoryOrderNumber) {
        this.categoryOrderNumber = categoryOrderNumber;
    }

    public String getCategoryIconCode() {
        return categoryIconCode;
    }

    public void setCategoryIconCode(String categoryIconCode) {
        this.categoryIconCode = categoryIconCode;
    }

    public String getCategoryCreateTimestamp() {
        return categoryCreateTimestamp;
    }

    public void setCategoryCreateTimestamp(String categoryCreateTimestamp) {
        this.categoryCreateTimestamp = categoryCreateTimestamp;
    }
}