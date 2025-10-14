package com.graduation.controller;

import com.graduation.entity.TaskFocus;
import com.graduation.service.TaskFocusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 任务专注表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/taskFocus")
public class TaskFocusController extends BaseController<TaskFocusService, TaskFocus> {

}
