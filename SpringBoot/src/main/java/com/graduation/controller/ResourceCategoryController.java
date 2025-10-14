package com.graduation.controller;

import com.graduation.entity.ResourceCategory;
import com.graduation.service.ResourceCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 资源分类表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/resourceCategory")
public class ResourceCategoryController extends BaseController<ResourceCategoryService, ResourceCategory> {

}
