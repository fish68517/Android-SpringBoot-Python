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
 * 校园好友表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("campus_friend")
public class CampusFriend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 校园好友标识
     */
    @TableId(value = "campus_friend_id", type = IdType.AUTO)
    private Long campusFriendId;

    /**
     * 用户 ID
     */
    private Long campusUserId;

    /**
     * 好友 ID
     */
    private Long friendUserId;

    /**
     * 好友状态 (pending, accepted, blocked)
     */
    private String friendStatusEnum;

    /**
     * 创建时间戳
     */
    private LocalDateTime friendCreateTimestamp;

    /**
     * 更新时间戳
     */
    private LocalDateTime friendUpdateTimestamp;

    /**
     * 是否可见对方动态
     */
    private Boolean friendVisibilityFlag;
}
