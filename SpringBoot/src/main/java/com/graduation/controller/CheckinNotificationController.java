package com.graduation.controller;

import com.graduation.dto.NotificationDTO;
import com.graduation.entity.CheckinNotification;
import com.graduation.repository.CheckinNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class CheckinNotificationController {

    @Autowired
    private CheckinNotificationRepository notificationRepository;

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
}