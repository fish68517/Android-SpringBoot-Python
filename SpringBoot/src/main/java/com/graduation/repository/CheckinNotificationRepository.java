package com.graduation.repository;

import com.graduation.dto.NotificationDTO;
import com.graduation.entity.CheckinNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckinNotificationRepository extends JpaRepository<CheckinNotification, Long> {

    /**
     * Finds all notifications and projects them into NotificationDTOs.
     * This query joins Notification -> Course -> Teacher(CampusUser) to get all required names.
     * The result is ordered by creation time to show the newest notifications first.
     *
     * @return A list of DTOs ready to be sent to the client.
     */
    /*@Query("SELECT new com.graduation.dto.NotificationDTO(n.id, c.courseName, u.username, n.creationTime, n.expirationTime, n.status, n.classroomPolygon) " +
            "FROM CheckinNotification n " +
            "JOIN n.course c " +
            "JOIN c.teacher u " +
            "ORDER BY n.creationTime DESC")
    List<NotificationDTO> findAllProjectedAsDto();*/



    @Query("SELECT new com.graduation.dto.NotificationDTO(n.id, c.courseName, u.username, n.creationTime, n.expirationTime, n.status, n.classroomPolygon, c.id) " +
            "FROM CheckinNotification n " +
            "JOIN n.course c " +
            "JOIN c.teacher u " +
            "ORDER BY n.creationTime DESC")
    List<NotificationDTO> findAllProjectedAsDto();

    // NOTE: In a real system with student course enrollments, the query would be more complex, like:
    // @Query("... FROM CheckinNotification n JOIN n.course c ... WHERE c.id IN (SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId) ...")
    // For now, we return all notifications and let the client have them.
}