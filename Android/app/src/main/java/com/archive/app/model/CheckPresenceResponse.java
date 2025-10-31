package com.archive.app.model;

import com.google.gson.annotations.SerializedName;

// 用于接收地理围栏响应的 POJO
public class CheckPresenceResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("is_present")
    private boolean isPresent;

    @SerializedName("distance_meters")
    private double distanceMeters;

    @SerializedName("message")
    private String message;

    // Getters
    public String getStatus() { return status; }
    public boolean isPresent() { return isPresent; }
    public double getDistanceMeters() { return distanceMeters; }
    public String getMessage() { return message; }
}