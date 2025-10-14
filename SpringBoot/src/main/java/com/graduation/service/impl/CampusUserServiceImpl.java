package com.graduation.service.impl;

import com.graduation.entity.CampusUser;
import com.graduation.mapper.CampusUserMapper;
import com.graduation.service.CampusUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 校园用户表 服务实现类
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Service
public class CampusUserServiceImpl extends ServiceImpl<CampusUserMapper, CampusUser> implements CampusUserService {

    @Autowired
    private CampusUserMapper campusUserMapper;
    @Override
    public CampusUser login(String email, String password) {
        return campusUserMapper.login(email, password);
    }
}
