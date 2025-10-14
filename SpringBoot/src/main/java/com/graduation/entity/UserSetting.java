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
 * 用户设置表
 * </p>
 *
 * @author 张三
 * @since 2025-10-02
 */
@Getter
@Setter
@TableName("user_setting")
public class UserSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户设置标识
     */
    @TableId(value = "user_setting_id", type = IdType.AUTO)
    private Long userSettingId;

    /**
     * 关联用户 ID
     */
    private Long campusUserId;

    /**
     * 主题 (light, dark)
     */
    private String settingThemeEnum;

    /**
     * 默认专注时长 (分钟)
     */
    private Integer settingDefaultFocusMins;

    /**
     * 默认休息时长 (分钟)
     */
    private Integer settingDefaultBreakMins;

    /**
     * 是否启用通知
     */
    private Boolean settingNotificationFlag;

    /**
     * 隐私级别 (0-公开, 1-好友可见, 2-私密)
     */
    private Byte settingPrivacyLevel;

    /**
     * 每日提醒时间
     */
    private LocalTime settingDailyReminderTime;

    /**
     * 周起始日 (sunday, monday)
     */
    private String settingWeekStartEnum;

    /**
     * 创建时间戳
     */
    private LocalDateTime settingCreateTimestamp;

    /**
     * 更新时间戳
     */
    private LocalDateTime settingUpdateTimestamp;
}
