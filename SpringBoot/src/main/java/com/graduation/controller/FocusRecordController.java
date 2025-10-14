package com.graduation.controller;

import com.graduation.entity.FocusRecord;
import com.graduation.service.FocusRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;

/**
 * <p>
 * 专注记录表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/focusRecord")
public class FocusRecordController extends BaseController<FocusRecordService, FocusRecord> {

}
