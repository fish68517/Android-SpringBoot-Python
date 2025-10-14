package com.graduation.controller;

import com.graduation.entity.UserSetting;
import com.graduation.service.UserSettingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 用户设置表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/userSetting")
public class UserSettingController extends BaseController<UserSettingService, UserSetting> {

}
