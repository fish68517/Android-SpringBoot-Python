package com.archive.app.model;

/**
 * 这个模型类用于构建“发起签到”请求的请求体。
 * Retrofit 会将这个类的一个实例序列化成 JSON 格式，然后通过 HTTP POST 请求发送给服务器。
 *
 * 示例 JSON 结构:
 * {
 *   "courseId": 101,
 *   "durationInMinutes": 15,
 *   "locationPolygon": "30.38480,114.19810;30.38480,114.19830;..."
 * }
 */
public class StartCheckinRequest {

    private Long courseId;
    private int durationInMinutes;
    private String locationPolygon;

    /**
     * 默认构造函数，一些序列化库需要它。
     */
    public StartCheckinRequest() {
    }

    /**
     * 便利的构造函数，用于快速创建实例。
     *
     * @param courseId          课程的ID
     * @param durationInMinutes 签到持续的分钟数
     * @param locationPolygon   地理围栏的坐标字符串
     */
    public StartCheckinRequest(Long courseId, int durationInMinutes, String locationPolygon) {
        this.courseId = courseId;
        this.durationInMinutes = durationInMinutes;
        this.locationPolygon = locationPolygon;
    }

    // --- Getters and Setters ---

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getLocationPolygon() {
        return locationPolygon;
    }

    public void setLocationPolygon(String locationPolygon) {
        this.locationPolygon = locationPolygon;
    }
}