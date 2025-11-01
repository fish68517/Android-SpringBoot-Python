package com.graduation.controller;

import com.graduation.entity.CheckinRecord;
import com.graduation.repository.CheckinRepository;
import com.graduation.service.CheckinRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/api/checkin/history")
public class CheckinRecordController extends BaseController<CheckinRecordService, com.graduation.entity.CheckinRecord> {

    @Autowired
    private CheckinRepository checkinRepository;
   // path 根据userId
    @RequestMapping("user/{userId}")
    public List<CheckinRecord> getByUserId(@PathVariable("userId") Long userId) {
        // 使用 List<...> 的调用代码 (推荐)
        List<CheckinRecord> records = checkinRepository.findByUserId(userId);

        System.out.println("查询到的签到记录数: " + records.size());
        if (records.isEmpty()) {
            System.out.println("该用户没有签到记录。");
            return new ArrayList<>();
        } else {
            return records;
        }

    }
}
