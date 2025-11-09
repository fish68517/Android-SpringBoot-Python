package com.graduation.controller;

import com.graduation.entity.CampusUser;
import com.graduation.repository.CampusUserRepository;
import com.graduation.service.CampusUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/campusUser")
public class CampusUserController extends BaseController<CampusUserService, com.graduation.entity.CampusUser> {

    @Autowired
    private CampusUserRepository userRepository;

    // 添加任何特定接口 login
    @RequestMapping("/login")
    public CampusUser login(@RequestBody com.graduation.entity.CampusUser user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())
                .map(u -> {
                    // 清除密码字段以防泄露
                    return u;
                })
                .orElse(null);

    }

    @RequestMapping("/login/username")
    public CampusUser loginForUsername(@RequestBody com.graduation.entity.CampusUser user) {
        return userRepository.findByUsername(user.getUsername())
                .map(u -> {
                    // 清除密码字段以防泄露
                    return u;
                })
                .orElse(null);

    }
}
