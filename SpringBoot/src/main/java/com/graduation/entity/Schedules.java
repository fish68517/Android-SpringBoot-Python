package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 张三
 * @since 2025-08-27
 */
@Getter
@Setter
public class Schedules implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "_id", type = IdType.AUTO)
    private Long id;

    private Integer serverId;

    private String title;

    private String description;

    private String location;

    private Long startTime;

    private Long endTime;

    private Integer reminderType;

    private String voiceTheme;

    private Boolean isCompleted;

    private Boolean isSynced;

    private Integer repeatMode;
}
