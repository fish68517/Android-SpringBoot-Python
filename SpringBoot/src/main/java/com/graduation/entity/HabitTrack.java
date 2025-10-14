package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 习惯追踪表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("habit_track")
public class HabitTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 习惯追踪标识
     */
    @TableId(value = "habit_track_id", type = IdType.AUTO)
    private Long habitTrackId;

    /**
     * 关联用户 ID
     */
    private Long campusUserId;

    /**
     * 习惯名称文本
     */
    private String habitNameText;

    /**
     * 频率 (daily, weekly)
     */
    private String habitFrequencyEnum;

    /**
     * 提醒时间
     */
    private LocalTime habitReminderTime;

    /**
     * 习惯状态 (active, archived)
     */
    private String habitStatusEnum;

    /**
     * 创建时间戳
     */
    private LocalDateTime habitCreateTimestamp;

    /**
     * 连续打卡天数
     */
    private Integer habitStreakCount;

    /**
     * 总打卡次数
     */
    private Integer habitTotalCount;

    /**
     * 目标打卡次数
     */
    private Integer habitGoalCount;

    /**
     * 习惯颜色 HEX
     */
    private String habitColorHex;

    /**
     * 习惯图标代码
     */
    private String habitIconCode;
}
