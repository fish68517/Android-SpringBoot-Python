package com.graduation.repository;


import com.graduation.entity.CheckinRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface CheckinRecordRepository extends JpaRepository<CheckinRecord, Long> {

    List<CheckinRecord> findByUserId(Long userId);

    List<CheckinRecord> findByCourseIdAndCheckinTimeBetween(Long courseId, Date start, Date end);

    int countByCourseIdAndStatusAndCheckinTimeBetween(Long courseId, String status, Date start, Date end);
}