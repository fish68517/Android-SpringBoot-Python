package com.graduation.controller;

import com.graduation.service.CourseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController<CourseService, com.graduation.entity.Course> {

}
