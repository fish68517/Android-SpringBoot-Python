/*
 Navicat Premium Dump SQL

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : student_checkin_db

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 04/03/2026 21:39:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for campus_user
-- ----------------------------
DROP TABLE IF EXISTS `campus_user`;
CREATE TABLE `campus_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `role` int NOT NULL COMMENT '1: 学生 (student), 2: 教师 (admin)',
  `school_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学号或工号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of campus_user
-- ----------------------------
INSERT INTO `campus_user` VALUES (1, '张老师', '123456', 'zhang.laoshi@teacher.edu', '13800138000', 2, 'T1001');
INSERT INTO `campus_user` VALUES (2, '李明', '123456', 'liming@student.edu', '13900139000', 1, 'S2021001');
INSERT INTO `campus_user` VALUES (3, '王芳', '123456', 'wangfang@student.edu', '13700137000', 1, 'S2021002');
INSERT INTO `campus_user` VALUES (4, '赵刚', '123456', 'zhaogang@student.edu', '13600136000', 1, 'S2021003');
INSERT INTO `campus_user` VALUES (5, '12', '12', NULL, NULL, 1, NULL);
INSERT INTO `campus_user` VALUES (6, '1', '1', 'zhaogang@student.edu', '13600136000', 1, 'S2021004');
INSERT INTO `campus_user` VALUES (7, '2', '123456', NULL, NULL, 1, NULL);
INSERT INTO `campus_user` VALUES (8, 'test_wangwu', '123456', NULL, NULL, 1, NULL);
INSERT INTO `campus_user` VALUES (9, 'marks', '123456', NULL, NULL, 1, NULL);
INSERT INTO `campus_user` VALUES (10, 'huge', '123456', NULL, NULL, 1, NULL);
INSERT INTO `campus_user` VALUES (11, 'zhoujielun', '123456', NULL, NULL, 1, NULL);
INSERT INTO `campus_user` VALUES (12, 'liydehua', '123456', NULL, NULL, 1, NULL);

-- ----------------------------
-- Table structure for checkin_notification
-- ----------------------------
DROP TABLE IF EXISTS `checkin_notification`;
CREATE TABLE `checkin_notification`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL COMMENT '关联的课程ID',
  `creation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知发起时间',
  `expiration_time` datetime NOT NULL COMMENT '签到截止时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '通知状态: ACTIVE (激活), EXPIRED (已过期), CANCELLED (已取消)',
  `classroom_polygon` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_course_id_notification`(`course_id` ASC) USING BTREE,
  CONSTRAINT `fk_notification_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教师发起的签到通知事件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of checkin_notification
-- ----------------------------
INSERT INTO `checkin_notification` VALUES (10, 3, '2025-11-09 15:33:49', '2025-11-09 15:43:49', 'ACTIVE', '[{\"latitude\":30.48195175333713,\"longitude\":114.53529981534143},{\"latitude\":30.48178070232253,\"longitude\":114.5335027353322},{\"latitude\":30.481292974130486,\"longitude\":114.53437981766507}]');
INSERT INTO `checkin_notification` VALUES (11, 2, '2025-11-09 16:36:00', '2025-11-09 16:46:00', 'ACTIVE', '[{\"latitude\":30.47995806575214,\"longitude\":114.53636197009317},{\"latitude\":30.479921081016975,\"longitude\":114.53657654681069},{\"latitude\":30.479766207285856,\"longitude\":114.53643975415326}]');
INSERT INTO `checkin_notification` VALUES (12, 2, '2025-11-09 16:37:53', '2025-11-09 16:47:53', 'ACTIVE', '[{\"latitude\":30.479963844615742,\"longitude\":114.53679917015512},{\"latitude\":30.479886407815002,\"longitude\":114.5368340388717},{\"latitude\":30.479956909979382,\"longitude\":114.53685549654347}]');
INSERT INTO `checkin_notification` VALUES (13, 4, '2025-11-09 17:08:09', '2025-11-09 17:18:09', 'ACTIVE', '[{\"latitude\":30.481186644437145,\"longitude\":114.535857714807},{\"latitude\":30.480958959811872,\"longitude\":114.5353293196401},{\"latitude\":30.48082835874586,\"longitude\":114.535813458359}]');

-- ----------------------------
-- Table structure for checkin_record
-- ----------------------------
DROP TABLE IF EXISTS `checkin_record`;
CREATE TABLE `checkin_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '学生ID, 关联 campus_user.id',
  `course_id` bigint NOT NULL COMMENT '课程ID, 关联 course.id',
  `checkin_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `latitude` double NULL DEFAULT NULL,
  `longitude` double NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '例如: 成功, 迟到, 缺勤',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `fk_record_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_record_user` FOREIGN KEY (`user_id`) REFERENCES `campus_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of checkin_record
-- ----------------------------
INSERT INTO `checkin_record` VALUES (1, 5, 1, '2025-10-27 08:02:00', 30.384723, 115.19816, '成功');
INSERT INTO `checkin_record` VALUES (2, 5, 1, '2025-11-01 08:08:00', 30.38472, 114.19815, '失败');
INSERT INTO `checkin_record` VALUES (3, 5, 1, '2025-11-03 08:00:00', 30.384723, 114.19816, '失败');
INSERT INTO `checkin_record` VALUES (4, 3, 1, '2025-11-03 08:01:00', 30.38472, 114.19815, '失败');
INSERT INTO `checkin_record` VALUES (5, 5, 1, '2025-11-03 08:03:00', 30.38471, 114.1981, '失败');
INSERT INTO `checkin_record` VALUES (6, 9, 2, '2025-11-06 10:05:00', 30.384723, 114.19816, '成功');
INSERT INTO `checkin_record` VALUES (7, 6, 2, '2025-10-28 10:11:00', 30.385173, 114.19816, '迟到');
INSERT INTO `checkin_record` VALUES (8, 10, 2, '2025-10-28 10:02:00', 30.38471, 114.1981, '成功');
INSERT INTO `checkin_record` VALUES (11, 6, 2, '2025-11-09 17:02:38', 30.479944, 114.536921, '成功');
INSERT INTO `checkin_record` VALUES (12, 9, 2, '2025-11-09 17:04:07', 30.479942, 114.536921, '成功');
INSERT INTO `checkin_record` VALUES (13, 9, 3, '2025-11-09 17:04:33', 30.479952, 114.536918, '失败');
INSERT INTO `checkin_record` VALUES (14, 12, 4, '2025-11-09 17:08:40', 30.479903, 114.536938, '失败');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `course_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `teacher_id` bigint NOT NULL COMMENT '外键, 关联 campus_user.id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_teacher_id`(`teacher_id` ASC) USING BTREE,
  CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `campus_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, '计算机网络', 'CS10102', 1);
INSERT INTO `course` VALUES (2, '操作系统', 'CS102', 1);
INSERT INTO `course` VALUES (3, '数学', 's01', 1);
INSERT INTO `course` VALUES (4, '大学物理 2', '111', 1);

SET FOREIGN_KEY_CHECKS = 1;
