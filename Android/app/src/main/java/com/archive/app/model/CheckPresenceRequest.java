package com.archive.app.model;

import com.amap.api.maps.model.LatLng;
import java.util.List;

// 用于发送地理围栏请求的 POJO
public class CheckPresenceRequest {

    // 注意：变量名必须和 Python API 接收的 JSON 键一致
    private LatLngs user_location;
    private List<LatLngs> classroom_polygon;
    private double threshold_meters;

    public CheckPresenceRequest(LatLngs user_location, List<LatLngs> classroom_polygon, double threshold_meters) {
        this.user_location = user_location;
        this.classroom_polygon = classroom_polygon;
        this.threshold_meters = threshold_meters;
    }

    // 内部类，用于匹配 (latitude, longitude) 格式
    public static class LatLngs {
        private double latitude;
        private double longitude;

        public LatLngs(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}