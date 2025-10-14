package com.graduation.controller;

import com.graduation.entity.LearnResource;
import com.graduation.service.LearnResourceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 学习资源表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/learnResource")
public class LearnResourceController extends BaseController<LearnResourceService, LearnResource> {

}
