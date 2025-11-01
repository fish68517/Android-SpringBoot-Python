package com.graduation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "checkin_notification")
public class CheckinNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A notification belongs to one course.
    // FetchType.LAZY is a performance best practice.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp // Automatically sets the value on creation
    @Column(name = "creation_time", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // Default status

    @Lob // Use @Lob for TEXT columns to support large strings
    @Column(name = "location_polygon")
    private String locationPolygon;
}