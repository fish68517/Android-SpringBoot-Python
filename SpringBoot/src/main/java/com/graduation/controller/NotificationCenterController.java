package com.graduation.controller;

import com.graduation.entity.NotificationCenter;
import com.graduation.service.NotificationCenterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 通知中心表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/notificationCenter")
public class NotificationCenterController extends BaseController<NotificationCenterService,
        NotificationCenter> {

}
