package com.graduation.mapper;

import com.graduation.entity.CampusUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 校园用户表 Mapper 接口
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Mapper
public interface CampusUserMapper extends BaseMapper<CampusUser> {

    /**
     * 根据邮箱和密码登录
     * @param email 邮箱地址
     * @param password 密码
     * @return 用户信息
     */
    CampusUser login(@Param("email") String email, @Param("password") String password);

}
