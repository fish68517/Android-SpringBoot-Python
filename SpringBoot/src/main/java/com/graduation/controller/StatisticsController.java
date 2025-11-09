package com.graduation.controller;

import com.graduation.dto.CheckinStatistics;
import com.graduation.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping("/daily")
    public CheckinStatistics getDailyStatistics(@RequestParam Long courseId, @RequestParam String date) {
        return statisticsService.getDailyStatistics(courseId, date);
    }

    @RequestMapping("/weekly")
    public CheckinStatistics getWeeklyStatistics(@RequestParam Long courseId, @RequestParam String week) {
        return statisticsService.getWeeklyStatistics(courseId, week);
    }

    @RequestMapping("/monthly")
    public CheckinStatistics getMonthlyStatistics(@RequestParam Long courseId, @RequestParam String month) {
        return statisticsService.getMonthlyStatistics(courseId, month);
    }
}