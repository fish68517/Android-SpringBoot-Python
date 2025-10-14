package com.graduation.controller;

import com.graduation.entity.UserAchieveRel;
import com.graduation.service.UserAchieveRelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 用户成就关联表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/userAchieveRel")
public class UserAchieveRelController extends BaseController<UserAchieveRelService, UserAchieveRel> {

}
