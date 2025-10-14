package com.graduation.controller;

import com.graduation.entity.CampusUser;
import com.graduation.service.CampusUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.graduation.common.BaseController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 校园用户表 前端控制器
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@RestController
@RequestMapping("/campusUser")
public class CampusUserController extends BaseController<CampusUserService, CampusUser> {

    @Autowired
    private CampusUserService campusUserService;

    /**
     * 用户登录接口
     * @return 登录结果（包含 token 或用户信息）
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String email = params.get("campusEmailAddr");
        String password = params.get("password");

        // 调用服务层进行登录验证
        CampusUser user = campusUserService.login(email, password);  // 假设服务层有此方法

        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            // 登录成功，生成 token 或返回用户 ID
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", user);
        } else {
            result.put("code", 400);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    // 增加 GET
    //	http://localhost:8081/campusUser/list
    @RequestMapping("/list")
    public Map<String, Object> getCampusUserList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<CampusUser> users = campusUserService.list();
            result.put("code", 200);
            result.put("message", "获取用户列表成功");
            result.put("data", users);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "用户列表加载失败");
            result.put("data", null);
        }
        return result;
    }

}