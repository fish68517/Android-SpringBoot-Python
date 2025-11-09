package com.graduation.service;

import com.graduation.dto.CheckinStatistics;
import com.graduation.repository.CampusUserRepository;
import com.graduation.repository.CheckinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Service
public class StatisticsService {

    @Autowired
    private CheckinRepository checkinRecordRepo;
    // 假设你有 UserRepository 来获取总学生数
    @Autowired
    private CampusUserRepository userRepo;

    public CheckinStatistics getStatisticsForRange(Long courseId, LocalDateTime startTime, LocalDateTime endTime) {
        // TODO: 获取该课程的总学生数
        // int totalStudents = userRepo.countByCourseId(courseId); // 这需要一个自定义查询
       //  int totalStudents = 10; // 暂时硬编码
        int totalStudents = checkinRecordRepo.findAll().size();

        int presentCount = checkinRecordRepo.countByCourseIdAndStatusAndCheckinTimeBetween(courseId, "成功", startTime, endTime);
        int lateCount = checkinRecordRepo.countByCourseIdAndStatusAndCheckinTimeBetween(courseId, "失败", startTime, endTime);


        return new CheckinStatistics(totalStudents, presentCount, lateCount);
    }

    public CheckinStatistics getDailyStatistics(Long courseId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr); // 格式: YYYY-MM-DD
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return getStatisticsForRange(courseId, start, end);
    }

    public CheckinStatistics getWeeklyStatistics(Long courseId, String weekStr) {
        // weekStr 示例: "2025-W44"
        // (此部分逻辑较复杂，先用 daily 替代)
        LocalDate date = LocalDate.now(); // 简化处理
        LocalDateTime start = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1).atStartOfDay();
        LocalDateTime end = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7).atTime(LocalTime.MAX);
        return getStatisticsForRange(courseId, start, end);
    }

    public CheckinStatistics getMonthlyStatistics(Long courseId, String monthStr) {
        // monthStr 示例: "2025-11"
        LocalDate date = LocalDate.parse(monthStr + "-01");
        LocalDateTime start = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX);
        return getStatisticsForRange(courseId, start, end);
    }
}