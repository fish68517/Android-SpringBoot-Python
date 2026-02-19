/*
 Navicat Premium Dump SQL

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : game_projct

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 20/11/2025 21:56:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment_likes
-- ----------------------------
DROP TABLE IF EXISTS `comment_likes`;
CREATE TABLE `comment_likes`  (
  `user_id` int NOT NULL COMMENT '点赞用户ID',
  `comment_id` int NOT NULL COMMENT '被点赞的评论ID',
  `liked_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`user_id`, `comment_id`) USING BTREE,
  INDEX `comment_id`(`comment_id` ASC) USING BTREE,
  CONSTRAINT `comment_likes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comment_likes_ibfk_2` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`comment_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment_likes
-- ----------------------------
INSERT INTO `comment_likes` VALUES (5, 1, '2025-11-03 15:41:01');
INSERT INTO `comment_likes` VALUES (6, 1, '2025-11-12 17:46:33');
INSERT INTO `comment_likes` VALUES (12, 9, '2025-11-17 09:02:11');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `game_id` int NOT NULL COMMENT '游戏ID',
  `user_id` int NOT NULL COMMENT '发表评论的用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '评论审核状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  `reviewed_by_admin_id` int NULL DEFAULT NULL COMMENT '审核管理员ID',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `game_id`(`game_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `reviewed_by_admin_id`(`reviewed_by_admin_id` ASC) USING BTREE,
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comments_ibfk_3` FOREIGN KEY (`reviewed_by_admin_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '游戏评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (1, 1, 4, '这款策略游戏太棒了，深度十足！', 'approved', '2025-11-03 15:41:01', 1);
INSERT INTO `comments` VALUES (3, 1, 5, '广告内容，xxx。', 'rejected', '2025-11-03 15:41:01', 1);
INSERT INTO `comments` VALUES (9, 11, 13, '00', 'approved', '2025-11-15 22:15:06', NULL);
INSERT INTO `comments` VALUES (11, 15, 12, '这是一款很好玩游戏', 'pending', '2025-11-17 09:01:56', NULL);
INSERT INTO `comments` VALUES (12, 13, 16, '00', 'pending', '2025-11-17 09:17:23', NULL);
INSERT INTO `comments` VALUES (13, 1, 16, '00', 'pending', '2025-11-17 09:17:41', NULL);

-- ----------------------------
-- Table structure for games
-- ----------------------------
DROP TABLE IF EXISTS `games`;
CREATE TABLE `games`  (
  `game_id` int NOT NULL AUTO_INCREMENT,
  `publisher_id` int NOT NULL COMMENT '发布者ID (关联用户表)',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '游戏名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '游戏信息描述',
  `cover_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '游戏封面图片URL',
  `other_image_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '其他游戏图片URL列表, 用英文逗号 , 分隔',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `submitted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `reviewed_by_admin_id` int NULL DEFAULT NULL COMMENT '审核管理员ID',
  `reviewed_at` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '游戏分类',
  PRIMARY KEY (`game_id`) USING BTREE,
  INDEX `publisher_id`(`publisher_id` ASC) USING BTREE,
  INDEX `reviewed_by_admin_id`(`reviewed_by_admin_id` ASC) USING BTREE,
  CONSTRAINT `games_ibfk_1` FOREIGN KEY (`publisher_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `games_ibfk_2` FOREIGN KEY (`reviewed_by_admin_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '游戏信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of games
-- ----------------------------
INSERT INTO `games` VALUES (1, 2, '星际远征', '一款科幻背景的策略游戏。', '/image/xingji_cover.jpg', '/image/xingji_1.jpg,/image/xingji_2.jpg,/image/xingji_3.jpg', 'approved', '2025-11-08 13:43:41', 1, NULL, '竞技类');
INSERT INTO `games` VALUES (2, 2, '秘境守护者', '一款奇幻RPG游戏，等待审核。', '/image/mijing_cover.png', NULL, 'approved', '2025-11-08 13:43:41', NULL, '2025-11-12 22:37:02', '策略类');
INSERT INTO `games` VALUES (3, 2, '城市天际线MOD', '非官方作品，已被退回。', '/image/mijing_cover.png', NULL, 'rejected', '2025-11-08 13:43:41', 6, '2025-11-14 12:51:22', '角色扮演');
INSERT INTO `games` VALUES (4, 3, '开心农场乐园', '休闲模拟经营游戏。', '/image/farm_cover.jpg', '/image/farm_1.jpg,/image/farm_2.jpg', 'approved', '2025-11-08 13:43:41', 6, '2025-11-14 12:51:16', '卡牌类');
INSERT INTO `games` VALUES (9, 9, 'DNF', 'DNF 是腾讯出品的一款很好玩游戏', '/img/ace94466-75ee-42a7-83a3-b4480f693646.jpeg', '/img/d464180b-1fc0-43cb-bb05-c7cc9c0664a8.jpg', 'approved', '2025-11-12 18:09:03', NULL, '2025-11-12 22:36:32', '动作类');
INSERT INTO `games` VALUES (10, 9, 'QQ斗地主', 'QQ斗地主QQ斗地主QQ斗地主QQ斗地主QQ斗地主QQ斗地主', '/img/1951fe12-d8a4-4d10-893e-1c3b5ecc1101.png', '/img/3582a815-b635-4c1c-a522-f8614c93d40a.jpeg', 'approved', '2025-11-14 11:28:24', NULL, NULL, '卡牌');
INSERT INTO `games` VALUES (11, 9, '吃鸡游戏', '吃鸡游戏吃鸡游戏吃鸡游戏吃鸡游戏', '/img/2138a78b-5682-40dd-b9ba-e18cc11e1736.png', '/img/569efb88-0199-4887-84d7-e9606e730743.jpeg', 'approved', '2025-11-14 11:58:19', NULL, NULL, '角色扮演');
INSERT INTO `games` VALUES (12, 9, 'QQ斗地主2222', 'QQ斗地主2222QQ斗地主2222QQ斗地主2222QQ斗地主2222QQ斗地主2222', '/img/20ee19f1-a0ba-424f-99bd-ecfbcbe8e286.jpeg', '/img/e8348273-aea0-404e-b1ca-913f8e64e655.jpg', 'rejected', '2025-11-14 12:38:26', 6, '2025-11-14 12:51:58', '卡牌');
INSERT INTO `games` VALUES (13, 9, '梦幻西游', '梦幻西游梦幻西游梦幻西游梦幻西游', '/img/bd287409-b6a8-4ef6-a117-a2034898d9cf.png', '/img/285d21d7-619f-4ab1-a26c-6a4deb5ff928.jpeg', 'approved', '2025-11-14 12:43:12', 14, '2025-11-17 08:59:54', '角色扮演');
INSERT INTO `games` VALUES (15, 12, '梦幻西游', '梦幻西游梦幻西游梦幻西游梦幻西游梦幻西游梦幻西游', '/img/ddf5d53e-7d09-4026-848a-ec4523ffbeee.png', '/img/20034a12-7dce-4724-9436-6a8dd9e0eaf6.png', 'pending', '2025-11-17 09:01:38', NULL, NULL, '角色扮演');

-- ----------------------------
-- Table structure for preferences
-- ----------------------------
DROP TABLE IF EXISTS `preferences`;
CREATE TABLE `preferences`  (
  `preference_id` int NOT NULL AUTO_INCREMENT,
  `preference_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '偏好名称 (如: RPG, 策略, 射击)',
  PRIMARY KEY (`preference_id`) USING BTREE,
  UNIQUE INDEX `preference_name`(`preference_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '偏好定义表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of preferences
-- ----------------------------
INSERT INTO `preferences` VALUES (5, '休闲 (Casual)');
INSERT INTO `preferences` VALUES (4, '模拟经营 (Simulation)');
INSERT INTO `preferences` VALUES (3, '第一人称射击 (FPS)');
INSERT INTO `preferences` VALUES (2, '策略 (Strategy)');
INSERT INTO `preferences` VALUES (1, '角色扮演 (RPG)');

-- ----------------------------
-- Table structure for recommendations
-- ----------------------------
DROP TABLE IF EXISTS `recommendations`;
CREATE TABLE `recommendations`  (
  `recommendation_id` int NOT NULL AUTO_INCREMENT,
  `admin_id` int NOT NULL COMMENT '操作推荐的管理员ID',
  `user_id` int NOT NULL COMMENT '被推荐的用户ID',
  `game_id` int NOT NULL COMMENT '被推荐的游戏ID',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '推荐理由',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`recommendation_id`) USING BTREE,
  INDEX `admin_id`(`admin_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `game_id`(`game_id` ASC) USING BTREE,
  CONSTRAINT `recommendations_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `recommendations_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `recommendations_ibfk_3` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员推荐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of recommendations
-- ----------------------------
INSERT INTO `recommendations` VALUES (1, 1, 4, 1, '根据您的偏好，这款策略游戏很适合您。', '2025-11-03 15:41:01');
INSERT INTO `recommendations` VALUES (2, 1, 4, 2, '根据您的偏好，这款RPG游戏可能符合您的口味。', '2025-11-08 13:26:16');

-- ----------------------------
-- Table structure for user_favorites
-- ----------------------------
DROP TABLE IF EXISTS `user_favorites`;
CREATE TABLE `user_favorites`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `game_id` int NOT NULL COMMENT '游戏ID',
  `favorited_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`user_id`, `game_id`) USING BTREE,
  INDEX `game_id`(`game_id` ASC) USING BTREE,
  CONSTRAINT `user_favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_favorites_ibfk_2` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_favorites
-- ----------------------------
INSERT INTO `user_favorites` VALUES (4, 1, '2025-11-03 15:41:01');
INSERT INTO `user_favorites` VALUES (5, 4, '2025-11-03 15:41:01');
INSERT INTO `user_favorites` VALUES (6, 1, '2025-11-12 17:56:48');
INSERT INTO `user_favorites` VALUES (8, 11, '2025-11-14 14:18:27');
INSERT INTO `user_favorites` VALUES (12, 15, '2025-11-17 09:01:45');
INSERT INTO `user_favorites` VALUES (16, 13, '2025-11-17 09:17:17');

-- ----------------------------
-- Table structure for user_preferences
-- ----------------------------
DROP TABLE IF EXISTS `user_preferences`;
CREATE TABLE `user_preferences`  (
  `user_id` int NOT NULL COMMENT '用户ID',
  `preference_id` int NOT NULL COMMENT '偏好ID',
  PRIMARY KEY (`user_id`, `preference_id`) USING BTREE,
  INDEX `preference_id`(`preference_id` ASC) USING BTREE,
  CONSTRAINT `user_preferences_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_preferences_ibfk_2` FOREIGN KEY (`preference_id`) REFERENCES `preferences` (`preference_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户偏好关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_preferences
-- ----------------------------
INSERT INTO `user_preferences` VALUES (4, 1);
INSERT INTO `user_preferences` VALUES (4, 2);
INSERT INTO `user_preferences` VALUES (5, 4);
INSERT INTO `user_preferences` VALUES (5, 5);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号（手机或邮箱）',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '加密后的密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `role` enum('user','publisher','admin') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户分类',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '$2a$10$T1U7P3fA3rI7G.B/qC8b7.nO5YkLzX9E4G.tP/cR.aW2G.eR/sY.o', '超级管理员', 'admin', '2025-11-03 15:41:01');
INSERT INTO `users` VALUES (2, 'publisher_a@example.com', '$2a$10$hP.aT5eS6cR.oA7gX.eX8.gE5YkLzX9E4G.tP/cR.aW2G.eR/sY.o', '游戏厂商A', 'publisher', '2025-11-03 15:41:01');
INSERT INTO `users` VALUES (3, 'publisher_b@example.com', '$2a$10$uI.bV6fT7dS.pB8hY.fA9.hF6YkLzX9E4G.tP/cR.aW2G.eR/sY.o', '游戏工作室B', 'publisher', '2025-11-03 15:41:01');
INSERT INTO `users` VALUES (4, 'user_wang@example.com', '$2a$10$vJ.cW7gU8eT.qC9iZ.gB0.iG7YkLzX9E4G.tP/cR.aW2G.eR/sY.o', '玩家小王', 'user', '2025-11-03 15:41:01');
INSERT INTO `users` VALUES (5, 'user_li@example.com', '$2a$10$wK.dX8hV9fU.rD0jZ.hC1.jH8YkLzX9E4G.tP/cR.aW2G.eR/sY.o', '玩家小李', 'user', '2025-11-03 15:41:01');
INSERT INTO `users` VALUES (6, '128900@qq.com', '$2a$10$4FLM3M3cPmypDCFdxB2t9.hyYDj.ysGOLumPRr05iCP8Moq6DLGR6', '管理员小张', 'admin', '2025-11-12 16:55:09');
INSERT INTO `users` VALUES (8, '1289@qq.com', '$2a$10$PORR4U3RLeI7FyqXa2hZV.JLNPyH3E4WvWzgWl4EjQnLUnSQ1DSDC', '游戏玩家小李111', 'user', '2025-11-12 16:56:49');
INSERT INTO `users` VALUES (9, 'publish@qq.com', '$2a$10$cDkSxCzuT40xHAd843ELV.WYjQD2a0RxeQirSjeoS2eFisLj56/BC', '发布者校长', 'publisher', '2025-11-12 18:07:27');
INSERT INTO `users` VALUES (11, '12890011publisher@qq.com', '$2a$10$6eY84h931zJJeYaUcV7wduclqHa9LsbzDFbWWPRLFuel7PHfUhYTy', '腾讯游戏', 'publisher', '2025-11-13 09:45:41');
INSERT INTO `users` VALUES (12, 'wangyepublisher@qq.com', '$2a$10$ycVNtzzsJS3CdnVbW5Kr0u/gfGDRAcIWJs0jwkkfQYTpzFHpZw9ZG', '网易游戏', 'publisher', '2025-11-13 10:06:00');
INSERT INTO `users` VALUES (13, '123456@qq.com', '$2a$10$uxxb9GwhRAhlgMH5fkQvXu/YtIeZekQvwYVAG7hJb/797iw8Vuj7G', '资深游戏玩家小与1', 'user', '2025-11-14 14:19:50');
INSERT INTO `users` VALUES (14, 'admin@163.com', '$2a$10$bDSGwjDJineiaCf.NuFKyexEJEVHM.mA0WgNvS50fOG6ekdImPrL6', 'admin', 'admin', '2025-11-17 08:59:25');
INSERT INTO `users` VALUES (15, '10086@qq.com', '$2a$10$kdE9YKRBNtTel/eJT7wVJO3tdo5QwnxLyAXLESYJn.ecehlGwZErC', '10086@qq.com', 'user', '2025-11-17 09:10:40');
INSERT INTO `users` VALUES (16, '123@qq.com', '$2a$10$rIYDOB34sWXMdnZuMob0W.4.0uVVjZe3Nf5rrXeMydffpZs5Tl3Dm', '1234@qq.com', 'user', '2025-11-17 09:16:05');

-- ----------------------------
-- Table structure for withdrawal_requests
-- ----------------------------
DROP TABLE IF EXISTS `withdrawal_requests`;
CREATE TABLE `withdrawal_requests`  (
  `request_id` int NOT NULL AUTO_INCREMENT,
  `game_id` int NOT NULL COMMENT '申请撤回的游戏ID',
  `publisher_id` int NOT NULL COMMENT '申请的发布者ID',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '结束游戏推荐信息 (撤回理由)',
  `status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '撤回审核状态',
  `requested_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `reviewed_by_admin_id` int NULL DEFAULT NULL COMMENT '审核管理员ID',
  `reviewed_at` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`request_id`) USING BTREE,
  INDEX `game_id`(`game_id` ASC) USING BTREE,
  INDEX `publisher_id`(`publisher_id` ASC) USING BTREE,
  INDEX `reviewed_by_admin_id`(`reviewed_by_admin_id` ASC) USING BTREE,
  CONSTRAINT `withdrawal_requests_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `withdrawal_requests_ibfk_2` FOREIGN KEY (`publisher_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `withdrawal_requests_ibfk_3` FOREIGN KEY (`reviewed_by_admin_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '游戏撤回审核表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of withdrawal_requests
-- ----------------------------
INSERT INTO `withdrawal_requests` VALUES (1, 3, 2, '内容涉及版权问题，申请撤回并删除数据。', 'pending', '2025-11-03 15:41:01', NULL, NULL);
INSERT INTO `withdrawal_requests` VALUES (2, 4, 3, '服务器进行为期一周的维护，申请暂时下架游戏。', 'approved', '2025-11-09 10:00:00', 1, '2025-11-09 11:30:00');
INSERT INTO `withdrawal_requests` VALUES (3, 1, 2, '计划推出2.0重制版，申请下架当前版本。', 'rejected', '2025-11-10 14:15:00', 1, '2025-11-10 16:00:00');

SET FOREIGN_KEY_CHECKS = 1;
