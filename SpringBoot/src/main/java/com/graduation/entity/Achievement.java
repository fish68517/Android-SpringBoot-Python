package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 成就徽章表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成就徽章标识
     */
    @TableId(value = "achievement_id", type = IdType.AUTO)
    private Long achievementId;

    /**
     * 徽章名称文本
     */
    private String achieveNameText;

    /**
     * 徽章描述文本
     */
    private String achieveDescriptionText;

    /**
     * 徽章图标 URL
     */
    private String achieveIconUrl;

    /**
     * 获取规则文本
     */
    private String achieveRuleText;

    /**
     * 徽章类型 (e.g., focus, habit, social)
     */
    private String achieveTypeEnum;

    /**
     * 创建时间戳
     */
    private LocalDateTime achieveCreateTimestamp;

    /**
     * 是否激活
     */
    private Boolean achieveActiveFlag;
}
