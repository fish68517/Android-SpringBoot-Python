package com.graduation.controller;

import com.graduation.entity.Schedules;
import com.graduation.service.SchedulesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-08-27
 */
@RestController
@RequestMapping("/schedules")
public class SchedulesController extends BaseController<SchedulesService, Schedules> {

}
