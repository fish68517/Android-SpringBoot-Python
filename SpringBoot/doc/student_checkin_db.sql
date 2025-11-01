-- ---
-- 1. 创建数据表
-- ---

-- 确保数据库存在
CREATE DATABASE IF NOT EXISTS `student_checkin_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `student_checkin_db`;

-- 1. 用户表 (学生和教师)
DROP TABLE IF EXISTS `checkin_record`;
DROP TABLE IF EXISTS `course`;
DROP TABLE IF EXISTS `campus_user`;

CREATE TABLE `campus_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL, -- 实际项目中应存储哈希值
  `email` VARCHAR(100),
  `phone` VARCHAR(20),
  `role` INT NOT NULL COMMENT '1: 学生 (student), 2: 教师 (admin)',
  `school_id` VARCHAR(50) COMMENT '学号或工号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. 课程表
CREATE TABLE `course` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `course_name` VARCHAR(100) NOT NULL,
  `course_code` VARCHAR(50),
  `teacher_id` BIGINT NOT NULL COMMENT '外键, 关联 campus_user.id',
  PRIMARY KEY (`id`),
  KEY `idx_teacher_id` (`teacher_id`),
  CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `campus_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. 签到记录表
CREATE TABLE `checkin_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '学生ID, 关联 campus_user.id',
  `course_id` BIGINT NOT NULL COMMENT '课程ID, 关联 course.id',
  `checkin_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `latitude` DOUBLE,
  `longitude` DOUBLE,
  `status` VARCHAR(20) NOT NULL COMMENT '例如: 成功, 迟到, 缺勤',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`),
  CONSTRAINT `fk_record_user` FOREIGN KEY (`user_id`) REFERENCES `campus_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_record_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ---
-- 2. 插入模拟中文数据
-- ---

-- 1. 插入用户 (1个教师, 3个学生)
-- 密码均为 '123456' (真实项目应哈希存储)
INSERT INTO `campus_user` (`username`, `password`, `email`, `phone`, `role`, `school_id`)
VALUES
('张老师', '123456', 'zhang.laoshi@teacher.edu', '13800138000', 2, 'T1001'),
('李明', '123456', 'liming@student.edu', '13900139000', 1, 'S2021001'),
('王芳', '123456', 'wangfang@student.edu', '13700137000', 1, 'S2021002'),
('赵刚', '123456', 'zhaogang@student.edu', '13600136000', 1, 'S2021003');

-- 2. 插入课程 (由张老师授课)
-- (假设张老师的 ID 是 1)
INSERT INTO `course` (`course_name`, `course_code`, `teacher_id`)
VALUES
('计算机网络', 'CS101', 1),
('操作系统', 'CS102', 1);

-- 3. 插入签到记录 (模拟一些历史数据)
-- (假设李明ID=2, 王芳ID=3, 赵刚ID=4; 计算机网络ID=1, 操作系统ID=2)

-- 计算机网络 (CS101) 的签到
INSERT INTO `checkin_record` (`user_id`, `course_id`, `checkin_time`, `latitude`, `longitude`, `status`)
VALUES
(2, 1, '2025-10-27 08:02:00', 30.384723, 114.19816, '成功'),
(3, 1, '2025-10-27 08:08:00', 30.384720, 114.19815, '迟到'),
-- 赵刚缺勤 (没有记录)
(2, 1, '2025-11-03 08:00:00', 30.384723, 114.19816, '成功'),
(3, 1, '2025-11-03 08:01:00', 30.384720, 114.19815, '成功'),
(4, 1, '2025-11-03 08:03:00', 30.384710, 114.19810, '成功');

-- 操作系统 (CS102) 的签到
INSERT INTO `checkin_record` (`user_id`, `course_id`, `checkin_time`, `latitude`, `longitude`, `status`)
VALUES
(2, 2, '2025-10-28 10:05:00', 30.384723, 114.19816, '成功'),
(3, 2, '2025-10-28 10:11:00', 30.385173, 114.19816, '迟到'),
(4, 2, '2025-10-28 10:02:00', 30.384710, 114.19810, '成功');

SELECT '模拟数据插入完毕' AS 'Status';