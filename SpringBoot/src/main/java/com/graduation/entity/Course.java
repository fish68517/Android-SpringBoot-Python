package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import jakarta.persistence.*;
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
@Entity
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_code")
    private String courseCode;


    // 这个 teacher 字段，在 Java 对象层面是一个完整的 CampusUser 对象
    // @JoinColumn 告诉 JPA：
    // “请用 course 表中的 teacher_id 列来建立这个关联关系”
    // “teacher_id 列的值，对应的是 CampusUser 表的主键 id”
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private CampusUser teacher;
}
