package com.graduation.controller;

import com.graduation.entity.CheckinRecord;
import com.graduation.repository.CheckinRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    @Autowired
    private CheckinRecordRepository checkinRecordRepo;

    @PostMapping
    public CheckinRecord saveCheckinRecord(@RequestBody CheckinRecord record) {
        if (record.getCheckinTime() == null) {
            record.setCheckinTime(LocalDateTime.now());
        }
        // 打印 record
        System.out.println("保存的签到记录: " + record);
        // @PrePersist 会自动设置 checkinTime
        return checkinRecordRepo.save(record);
    }

    @RequestMapping("/history/{studentId}")
    public List<CheckinRecord> getCheckinHistory(@PathVariable Long studentId) {
        return checkinRecordRepo.findByUserId(studentId);
    }

    /**
     * TODO: 实现真实的签到通知逻辑
     * (可能需要一个 'checkin_session' 表)
     */
    @RequestMapping("/notifications/{studentId}")
    public List<Object> getCheckinNotifications(@PathVariable Long studentId) {
        // 模拟一个激活的签到
        return new ArrayList<>(); // 返回空列表
    }

    /**
     * TODO: 实现真实的发起签到逻辑
     * (这应该创建一个 'checkin_session' 记录，并设置过期时间)
     */
    @PostMapping("/start")
    public ResponseEntity<Void> startCheckin(@RequestParam Long courseId, @RequestParam int durationMinutes) {
        System.out.println("教师发起了课程 " + courseId + " 的签到，持续 " + durationMinutes + " 分钟。");
        // (在这里添加创建签到会话的逻辑)
        return ResponseEntity.ok().build();
    }
}