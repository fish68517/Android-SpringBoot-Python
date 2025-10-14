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
 * 专注记录表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("focus_record")
public class FocusRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专注记录标识
     */
    @TableId(value = "focus_record_id", type = IdType.AUTO)
    private Long focusRecordId;

    /**
     * 关联用户 ID
     */
    private Long campusUserId;

    /**
     * 关联任务 ID
     */
    private Long taskFocusId;

    /**
     * 开始时间戳
     */
    private LocalDateTime focusStartTimestamp;

    /**
     * 结束时间戳
     */
    private LocalDateTime focusEndTimestamp;

    /**
     * 专注时长 (秒)
     */
    private Integer focusDurationSeconds;

    /**
     * 专注类型 (e.g., pomodoro, custom)
     */
    private String focusTypeEnum;

    /**
     * 专注状态 (completed, interrupted)
     */
    private String focusStatusEnum;

    /**
     * 中断次数
     */
    private Integer focusInterruptionCount;

    /**
     * 暂停次数
     */
    private Integer focusPauseCount;

    /**
     * 专注备注文本
     */
    private String focusNoteText;

    /**
     * 是否启用应用拦截
     */
    private Boolean focusAppBlockFlag;
}
