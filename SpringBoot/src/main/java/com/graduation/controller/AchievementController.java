package com.graduation.controller;

import com.graduation.entity.Achievement;
import com.graduation.service.AchievementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 成就徽章表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/achievement")
public class AchievementController extends BaseController<AchievementService, Achievement> {

}
