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
 * 用户成就关联表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("user_achieve_rel")
public class UserAchieveRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户成就关联标识
     */
    @TableId(value = "user_achieve_id", type = IdType.AUTO)
    private Long userAchieveId;

    /**
     * 关联用户 ID
     */
    private Long campusUserId;

    /**
     * 关联徽章 ID
     */
    private Long achievementId;

    /**
     * 获得时间戳
     */
    private LocalDateTime achieveTimestamp;

    /**
     * 达成条件描述
     */
    private String achieveConditionText;
}
