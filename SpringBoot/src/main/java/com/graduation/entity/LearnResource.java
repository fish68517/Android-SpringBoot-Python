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
 * 学习资源表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("learn_resource")
public class LearnResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学习资源标识
     */
    @TableId(value = "learn_resource_id", type = IdType.AUTO)
    private Long learnResourceId;

    /**
     * 资源分类 ID
     */
    private Long resourceCategoryId;

    /**
     * 资源名称文本
     */
    private String resourceNameText;

    /**
     * 资源链接文本
     */
    private String resourceUrlText;

    /**
     * 资源描述文本
     */
    private String resourceDescriptionText;

    /**
     * 资源类型 (link, document)
     */
    private String resourceTypeEnum;

    /**
     * 推荐级别 (0-5)
     */
    private Byte resourceRecommendLevel;

    /**
     * 创建时间戳
     */
    private LocalDateTime resourceCreateTimestamp;

    /**
     * 更新时间戳
     */
    private LocalDateTime resourceUpdateTimestamp;

    /**
     * 点击次数
     */
    private Integer resourceClickCount;
}
