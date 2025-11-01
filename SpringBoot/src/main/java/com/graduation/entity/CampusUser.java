package com.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@Entity // 1. 必须添加 @Entity 注解，告诉 JPA 这是一个实体类
@Table(name = "campus_user") // 2. 使用 @Table 指定表名，作用等同于 @TableName
public class CampusUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Id // 3. 标记为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. 标记为自增主键，等同于 IdType.AUTO
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    /**
     * 1: 学生 (student), 2: 教师 (admin)
     */
    private Integer role;

    /**
     * 学号或工号
     */
    /**
     * 学号或工号
     */
    @Column(name = "school_id") // 5. 如果数据库列名和属性名不一致（驼峰<->下划线），建议用 @Column 明确指定
    private String schoolId;
}
