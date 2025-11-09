package com.graduation.repository;


import com.graduation.entity.CampusUser;
import com.graduation.entity.CheckinRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckinRepository extends JpaRepository<CheckinRecord, Long> {
    // 根据用户 UserId 查找签到记录
    // 推荐: 直接返回 List
    // Spring Data JPA 保证这个方法永远不会返回 null。
    // 如果没有找到记录，它会返回一个空的 List。
    List<CheckinRecord> findByUserId(Long userId);



    List<CheckinRecord> findByCourseIdAndCheckinTimeBetween(Long courseId, LocalDateTime start, LocalDateTime end);

    int countByCourseIdAndStatusAndCheckinTimeBetween(Long courseId, String status, LocalDateTime start, LocalDateTime end);
}