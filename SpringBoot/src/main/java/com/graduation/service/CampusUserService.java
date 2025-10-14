package com.graduation.service;

import com.graduation.entity.CampusUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 校园用户表 服务类
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
public interface CampusUserService extends IService<CampusUser> {


    // 增加登陆接口
    CampusUser login(String email, String password);
}
