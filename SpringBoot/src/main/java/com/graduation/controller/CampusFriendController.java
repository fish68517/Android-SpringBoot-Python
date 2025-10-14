package com.graduation.controller;

import com.graduation.entity.CampusFriend;
import com.graduation.service.CampusFriendService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 校园好友表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/campusFriend")
public class CampusFriendController extends BaseController<CampusFriendService, CampusFriend> {

}
