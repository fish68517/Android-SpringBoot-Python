package com.graduation.dto;

import lombok.Data;

@Data
public class StartCheckinRequest {
    private Long courseId;
    private int durationInMinutes;
    private String locationPolygon;
}