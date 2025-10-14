package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 习惯打卡表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("habit_checkin")
public class HabitCheckin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 习惯打卡标识
     */
    @TableId(value = "habit_checkin_id", type = IdType.AUTO)
    private Long habitCheckinId;

    /**
     * 关联习惯 ID
     */
    private Long habitTrackId;

    /**
     * 打卡日期
     */
    private LocalDate checkinDate;

    /**
     * 打卡时间戳
     */
    private LocalDateTime checkinTimestamp;

    /**
     * 打卡状态 (completed, skipped)
     */
    private String checkinStatusEnum;

    /**
     * 打卡备注文本
     */
    private String checkinNoteText;

    /**
     * 当前连续打卡数
     */
    private Integer checkinStreakCount;
}
