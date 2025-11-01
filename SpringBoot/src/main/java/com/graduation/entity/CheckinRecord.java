package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 张三
 * @since 2025-11-01
 */
@Getter
@Setter
@TableName("checkin_record")
@Entity
public class CheckinRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生ID, 关联 campus_user.id
     */
    private Long userId;

    /**
     * 课程ID, 关联 course.id
     */
    private Long courseId;

    private LocalDateTime checkinTime;

    private Double latitude;

    private Double longitude;

    /**
     * 例如: 成功, 迟到, 缺勤
     */
    private String status;
}
