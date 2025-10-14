package com.graduation.controller;

import com.graduation.entity.HabitCheckin;
import com.graduation.service.HabitCheckinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 习惯打卡表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/habitCheckin")
public class HabitCheckinController extends BaseController<HabitCheckinService, HabitCheckin> {

}
