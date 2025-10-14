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
 * 通知中心表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("notification_center")
public class NotificationCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知标识
     */
    @TableId(value = "notification_id", type = IdType.AUTO)
    private Long notificationId;

    /**
     * 关联用户 ID
     */
    private Long campusUserId;

    /**
     * 通知类型 (system, friend_request, task_reminder)
     */
    private String notificationTypeEnum;

    /**
     * 通知标题文本
     */
    private String notificationTitleText;

    /**
     * 通知内容文本
     */
    private String notificationContentText;

    /**
     * 是否已读标志
     */
    private Boolean notificationIsReadFlag;

    /**
     * 创建时间戳
     */
    private LocalDateTime notificationCreateTimestamp;

    /**
     * 过期时间戳
     */
    private LocalDateTime notificationExpireTimestamp;

    /**
     * 动作 URL
     */
    private String notificationActionUrl;

    /**
     * 优先级代码 (0 - 低, 1 - 中, 2 - 高)
     */
    private Byte notificationPriorityCode;
}
