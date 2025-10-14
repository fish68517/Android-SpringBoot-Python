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
 * 资源分类表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("resource_category")
public class ResourceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源分类标识
     */
    @TableId(value = "resource_category_id", type = IdType.AUTO)
    private Long resourceCategoryId;

    /**
     * 分类名称文本
     */
    private String categoryNameText;

    /**
     * 分类描述文本
     */
    private String categoryDescriptionText;

    /**
     * 显示顺序号
     */
    private Integer categoryOrderNumber;

    /**
     * 分类图标代码
     */
    private String categoryIconCode;

    /**
     * 创建时间戳
     */
    private LocalDateTime categoryCreateTimestamp;
}
