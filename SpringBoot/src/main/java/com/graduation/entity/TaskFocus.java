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
 * 任务专注表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("task_focus")
public class TaskFocus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务专注标识
     */
    @TableId(value = "task_focus_id", type = IdType.AUTO)
    private Long taskFocusId;

    /**
     * 关联用户 ID
     */
    private Long campusUserId;

    /**
     * 任务标题文本
     */
    private String taskTitleText;

    /**
     * 任务描述文本
     */
    private String taskDescriptionText;

    /**
     * 截止时间戳
     */
    private LocalDateTime taskDeadlineTimestamp;

    /**
     * 任务状态 (pending, in_progress, completed, archived)
     */
    private String taskStatusEnum;

    /**
     * 创建时间戳
     */
    private LocalDateTime taskCreateTimestamp;

    /**
     * 优先级代码 (0 - 低, 1 - 中, 2 - 高)
     */
    private Byte taskPriorityCode;

    /**
     * 提醒时间戳
     */
    private LocalDateTime taskReminderTimestamp;

    /**
     * 任务分类代码
     */
    private String taskCategoryCode;

    /**
     * 专注时长 (分钟)
     */
    private Integer taskFocusDurationMins;

    /**
     * 休息时长 (分钟)
     */
    private Integer taskBreakDurationMins;

    /**
     * 是否启用应用拦截
     */
    private Boolean taskAppBlockFlag;

    /**
     * 实际分钟数
     */
    private Integer taskActualMinutes;
}
