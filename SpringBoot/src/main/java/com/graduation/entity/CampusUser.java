package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 校园用户表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("campus_user")
public class CampusUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 校园用户唯一标识
     */
    @TableId(value = "campus_user_id", type = IdType.AUTO)
    private Long campusUserId;

    /**
     * 用户昵称
     */
    private String campusNickname;

    private String password;

    /**
     * 头像 URL
     */
    private String campusAvatarUrl;

    /**
     * 学号
     */
    private String campusSchoolId;

    /**
     * 邮箱地址
     */
    private String campusEmailAddr;

    /**
     * 状态 (0 - 未审核, 1 - 已审核, 2 - 禁用)
     */
    private Byte campusStatusFlag;

    /**
     * 创建时间戳
     */
    private LocalDateTime campusCreateTimestamp;

    /**
     * 用户等级代码
     */
    private Integer campusLevelCode;

    /**
     * 用户徽章个数
     */
    private Integer campusBadgeCount;

    /**
     * 最后登录时间
     */
    private LocalDateTime campusLastLoginTs;

    /**
     * 用户类型 (student, admin)
     */
    private String campusUserType;
}
