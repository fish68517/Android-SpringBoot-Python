package com.graduation.controller;

import com.graduation.dto.NotificationDTO;
import com.graduation.dto.StartCheckinRequest;
import com.graduation.entity.CheckinNotification;
import com.graduation.entity.Course;
import com.graduation.repository.CheckinNotificationRepository;
import com.graduation.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class CheckinNotificationController {

    @Autowired
    private CheckinNotificationRepository notificationRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * CREATE: A teacher creates a new check-in notification.
     */
    @PostMapping
    public CheckinNotification createNotification(@RequestBody CheckinNotification notification) {
        // In a real application, you would set the Course object here based on a courseId
        // For example: notification.setCourse(courseRepository.findById(courseId).get());
        return notificationRepository.save(notification);
    }

    /**
     * READ: Get all notifications formatted for a student client.
     * Corresponds to /api/notifications/student/{studentId}
     *
     * IMPORTANT: Current implementation returns ALL notifications.
     * A full implementation would require filtering based on the student's enrolled courses.
     */
    @GetMapping("/student/{studentId}")
    public List<NotificationDTO> getNotificationsForStudent(@PathVariable Long studentId) {
        // TODO: Add logic here to filter notifications based on which courses the studentId is enrolled in.
        // For this example, we return all notifications as projected DTOs.
        return notificationRepository.findAllProjectedAsDto();
    }

    /**
     * READ: Get a single notification by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CheckinNotification> getNotificationById(@PathVariable Long id) {
        return notificationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * UPDATE: Update an existing notification (e.g., extend time, change status).
     */
    @PutMapping("/{id}")
    public ResponseEntity<CheckinNotification> updateNotification(@PathVariable Long id, @RequestBody CheckinNotification notificationDetails) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setExpirationTime(notificationDetails.getExpirationTime());
                    notification.setStatus(notificationDetails.getStatus());
                    notification.setLocationPolygon(notificationDetails.getLocationPolygon());
                    // We don't allow changing the course
                    return ResponseEntity.ok(notificationRepository.save(notification));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE: Cancel or remove a notification.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (!notificationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        notificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * CREATE: 老师为指定课程发起签到.
     * @param notificationRequest 包含课程ID和持续时间的请求体
     * @return 创建的通知实体
     */
    @PostMapping("/start")
    public ResponseEntity<CheckinNotification> startCheckin(@RequestBody StartCheckinRequest notificationRequest) {

        // 1. 根据 courseId 查找课程实体
        Course course = courseRepository.findById(notificationRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + notificationRequest.getCourseId()));

        // 2. 创建并设置 CheckinNotification 实体
        CheckinNotification notification = new CheckinNotification();
        notification.setCourse(course);
        notification.setCreationTime(LocalDateTime.now());
        notification.setExpirationTime(LocalDateTime.now().plusMinutes(notificationRequest.getDurationInMinutes()));
        notification.setStatus("ACTIVE");
        notification.setLocationPolygon(notificationRequest.getLocationPolygon());

        // 3. 保存到数据库
        CheckinNotification savedNotification = notificationRepository.save(notification);
        return ResponseEntity.ok(savedNotification);
    }
}