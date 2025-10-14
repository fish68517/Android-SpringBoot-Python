package com.graduation.controller;

import com.graduation.entity.HabitTrack;
import com.graduation.service.HabitTrackService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 习惯追踪表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/habitTrack")
public class HabitTrackController extends BaseController<HabitTrackService, HabitTrack> {

}
