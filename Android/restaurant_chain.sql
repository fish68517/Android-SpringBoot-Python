/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : restaurant_chain

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 07/06/2025 14:44:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_items
-- ----------------------------
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `dish_id` int NOT NULL,
  `user_id` int NOT NULL,
  `quantity` int NOT NULL,
  `selected_options` json NULL,
  `total_price` decimal(10, 2) NOT NULL,
  `cart_type` enum('预约点单','到店消费') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_items
-- ----------------------------

-- ----------------------------
-- Table structure for coupons
-- ----------------------------
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons`  (
  `coupon_id` int NOT NULL AUTO_INCREMENT,
  `coupon_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `coupon_type` tinyint NULL DEFAULT NULL COMMENT '1:满减 2:折扣',
  `min_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '最低使用金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `discount_rate` decimal(3, 2) NULL DEFAULT NULL COMMENT '折扣率',
  `valid_days` int NULL DEFAULT NULL COMMENT '有效期天数',
  `status` tinyint NULL DEFAULT 1 COMMENT '1:有效 0:无效',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupons
-- ----------------------------
INSERT INTO `coupons` VALUES (2, '满50减10', 1, NULL, NULL, NULL, NULL, 1);
INSERT INTO `coupons` VALUES (3, '85折优惠', 1, NULL, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for dish_categories
-- ----------------------------
DROP TABLE IF EXISTS `dish_categories`;
CREATE TABLE `dish_categories`  (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sort_order` int NULL DEFAULT 0,
  `status` tinyint NULL DEFAULT 1 COMMENT '1:启用 0:禁用',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish_categories
-- ----------------------------
INSERT INTO `dish_categories` VALUES (1, '热销推荐', 1, 1);
INSERT INTO `dish_categories` VALUES (2, '奶茶', 2, 1);
INSERT INTO `dish_categories` VALUES (3, '果茶', 3, 1);
INSERT INTO `dish_categories` VALUES (4, '咖啡', 4, 1);

-- ----------------------------
-- Table structure for dishes
-- ----------------------------
DROP TABLE IF EXISTS `dishes`;
CREATE TABLE `dishes`  (
  `dish_id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NULL DEFAULT NULL,
  `store_id` int NULL DEFAULT NULL,
  `dish_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '1:上架 0:下架',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `dish_options` json NULL,
  PRIMARY KEY (`dish_id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  INDEX `dishes_ibfk_3`(`store_id` ASC) USING BTREE,
  CONSTRAINT `dishes_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `dish_categories` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `dishes_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `dishes_ibfk_3` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dishes
-- ----------------------------
INSERT INTO `dishes` VALUES (1, 1, 1, '招牌奶茶', 28.00, '采用优质茶叶和新鲜牛奶精心调制,口感浓郁醇厚,香气扑鼻,让人回味无穷的招牌奶茶', 'jiushui_baixiangguo', 1, '2025-01-28 21:40:13', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"甜度\": [\"三分糖\", \"标准糖\", \"正常糖\"], \"辅料\": [\"加椰果\", \"加珍珠\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (2, 1, 2, '红茶拿铁', 32.00, '选用优质红茶与新鲜牛奶完美融合,口感丰富浓郁,香气迷人,是早晨或下午茶的最佳选择', 'jiushui_natie', 1, '2025-01-28 21:40:13', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (3, 2, 3, '抹茶拿铁', 26.00, '采用上等抹茶粉与牛奶调制而成,口感清新醇厚,香气悠长,让人沉浸其中,尽享清新怡人的滋味', 'jiushui_baitaowulong', 1, '2025-01-28 21:40:13', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (4, 3, 3, '草莓果茶', 16.00, '以新鲜草莓为基底,搭配优质茶叶和果汁调制而成,酸甜适中,清新爽口,让人欲罢不能的草莓果茶', 'jiushui_mitaosijichun', 1, '2025-01-28 21:40:13', '{\"容量\": [\"小份\", \"中份\", \"大份\"]}');
INSERT INTO `dishes` VALUES (5, 4, 2, '冰美式咖啡', 12.00, '采用优质阿拉比卡咖啡豆,经过精心烘焙和冲泡,口感醇厚浓郁,香气扑鼻,加上冰块制成的冰美式咖啡,清爽解渴,是夏日的最佳饮品', 'jiushui_fengmiyouzi', 1, '2025-01-28 21:40:13', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"冷饮\"]}');
INSERT INTO `dishes` VALUES (6, 1, 1, '焦糖玛奇朵', 28.00, '以浓郁的意式浓缩咖啡为基底,加入香醇的牛奶和焦糖调制而成,口感丰富层次分明,香气迷人,是咖啡爱好者的最爱', 'jiushui_natie', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (7, 1, 2, '芝士奶盖茶', 32.00, '采用优质茶叶为基底,加入浓郁的奶油芝士,口感丰富绵密,香气四溢,让人欲罢不能的奶盖茶', 'jiushui_baitaowulong', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"甜度\": [\"三分糖\", \"标准糖\", \"正常糖\"], \"辅料\": [\"加椰果\", \"加珍珠\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (8, 2, 3, '黑糖珍珠奶茶', 26.00, '以新鲜黑糖为基底,搭配Q弹可口的珍珠,再加入香醇的奶茶,口感丰富层次分明,回味无穷的黑糖珍珠奶茶', 'jiushui_mitaosijichun', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"甜度\": [\"三分糖\", \"标准糖\", \"正常糖\"], \"辅料\": [\"加椰果\", \"加珍珠\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (9, 3, 3, '芒果冰沙', 16.00, '采用新鲜芒果为主料,加入冰块和少许糖分调制而成,口感清爽可口,清甜不腻,是夏日消暑解渴的绝佳选择', 'jiushui_fengmiyouzi', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"]}');
INSERT INTO `dishes` VALUES (10, 4, 1, '冰咖啡', 12.00, '采用优质阿拉比卡咖啡豆,经过精心烘焙和冲泡,加入冰块制成,口感醇厚浓郁,香气扑鼻,清爽解渴,是咖啡爱好者的最佳选择', 'jiushui_baixiangguo', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"冷饮\"]}');
INSERT INTO `dishes` VALUES (11, 1, 2, '香草拿铁', 22.00, '以香醇浓郁的意式浓缩咖啡为基底,加入香草风味的牛奶,口感丰富层次分明,香气迷人,让人沉浸其中,尽享咖啡的魅力', 'jiushui_natie', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (12, 2, 3, '芝士奶盖咖啡', 18.00, '采用优质阿拉比卡咖啡豆为基底,加入浓郁的奶油芝士,口感丰富绵密,香气四溢,让人欲罢不能的奶盖咖啡', 'jiushui_mitaosijichun', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"热饮\", \"冷饮\"]}');
INSERT INTO `dishes` VALUES (13, 3, 1, '草莓果汁', 14.00, '以新鲜多汁的草莓为主料,搭配少许糖分和柠檬汁调制而成,口感清新甜美,清爽解渴,是夏日里的最佳饮品', 'jiushui_baitaowulong', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"]}');
INSERT INTO `dishes` VALUES (14, 4, 2, '冰摩卡', 15.00, '以浓郁的巧克力和意式浓缩咖啡为基底,加入冰块制成,口感丰富层次分明,香醇浓郁,让人沉醉其中的冰摩卡', 'jiushui_baixiangguo', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"], \"饮用\": [\"冷饮\"]}');
INSERT INTO `dishes` VALUES (15, 1, 3, '柠檬茶', 20.00, '采用优质茶叶为基底,加入新鲜柠檬汁和少许糖分调制而成,口感清新爽口,酸甜适中,是夏日里清凉解渴的最佳选择', 'jiushui_natie', 1, '2025-01-01 00:00:00', '{\"容量\": [\"小份\", \"中份\", \"大份\"]}');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `store_id` int NULL DEFAULT NULL,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `total_amount` decimal(10, 2) NOT NULL,
  `delivery_fee` decimal(10, 2) NULL DEFAULT 0.00,
  `status` tinyint NULL DEFAULT 0 COMMENT '0:待支付 1:已支付 2:配送中 3:已完成 4:已取消',
  `order_type` tinyint NULL DEFAULT NULL COMMENT '1:堂食 2:外卖',
  `delivery_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `payment_method` tinyint NULL DEFAULT NULL COMMENT '1:微信 2:支付宝',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `cart_items` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `store_id`(`store_id` ASC) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (29, 1, 1, 'd17e0688-8', 86.00, 0.00, 1, 2, NULL, NULL, NULL, '1:2:{容量=小份, 甜度=三分糖, 饮用=冷饮};7:1:{容量=大份}');
INSERT INTO `orders` VALUES (30, 1, 1, 'a33b7876-f', 56.00, 0.00, 1, 1, NULL, NULL, NULL, '8:2:{容量=中份, 辅料=加珍珠}');
INSERT INTO `orders` VALUES (31, 1, 1, '17428d81-3', 34.00, 0.00, 1, 2, NULL, NULL, NULL, '14:2:{容量=大份}');
INSERT INTO `orders` VALUES (32, 1, 1, '91d98f33-2', 68.00, 0.00, 1, 2, NULL, NULL, NULL, '1:1:{容量=大份, 辅料=加珍珠, 饮用=热饮};4:1:{容量=大份};12:1:{容量=中份, 饮用=热饮}');
INSERT INTO `orders` VALUES (33, 1, 1, '650a4b7c-7', 102.00, 0.00, 0, 2, NULL, NULL, '2025-02-03 17:49:41', '7:3:{容量=大份, 甜度=三分糖, 饮用=热饮}');
INSERT INTO `orders` VALUES (34, 1, 1, 'b4a50c77-6', 52.00, 0.00, 1, 2, NULL, NULL, '2025-02-10 15:05:26', '4:2:{容量=小份};3:1:{容量=小份, 饮用=热饮}');
INSERT INTO `orders` VALUES (35, 6, 1, '5aa0dc01-c', 30.00, 0.00, 1, 2, NULL, NULL, '2025-03-05 20:37:56', '1:1:{甜度=标准糖, 辅料=加椰果}');
INSERT INTO `orders` VALUES (36, 6, 1, '5d73881e-2', 32.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 20:50:19', '7:1:{甜度=标准糖}');
INSERT INTO `orders` VALUES (37, 6, 1, 'ec40796e-b', 22.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:13:09', '11:1:{容量=中份}');
INSERT INTO `orders` VALUES (38, 6, 1, 'be9b5a79-0', 26.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:21:20', '1:1:{容量=小份}');
INSERT INTO `orders` VALUES (39, 6, 1, '5a71ce6f-9', 28.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:22:59', '6:1:{饮用=热饮}');
INSERT INTO `orders` VALUES (40, 6, 1, '9d75ac27-9', 30.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:24:20', '1:1:{辅料=加珍珠}');
INSERT INTO `orders` VALUES (41, 6, 1, '7c1f8c5d-4', 26.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:25:51', '1:1:{容量=小份}');
INSERT INTO `orders` VALUES (42, 6, 1, 'e8f039a6-b', 26.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:28:33', '1:1:{容量=小份}');
INSERT INTO `orders` VALUES (43, 6, 1, 'c41597a4-0', 26.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:30:28', '1:1:{容量=小份}');
INSERT INTO `orders` VALUES (44, 6, 1, '95e50f42-d', 26.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:32:18', '1:1:{容量=小份}');
INSERT INTO `orders` VALUES (45, 6, 1, 'fa28a7bc-1', 14.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:35:02', '4:1:{容量=小份}');
INSERT INTO `orders` VALUES (46, 6, 1, 'c6e0fb8f-c', 28.00, 0.00, 1, 2, NULL, NULL, '2025-03-05 21:37:25', '1:1:{}');
INSERT INTO `orders` VALUES (47, 6, 1, '135dfe41-a', 28.00, 0.00, 1, 1, NULL, NULL, '2025-03-05 21:53:38', '1:1:{甜度=三分糖}');
INSERT INTO `orders` VALUES (48, 6, 1, '4515a352-0', 24.00, 0.00, 1, 1, NULL, NULL, '2025-03-08 18:36:28', '8:1:{容量=小份}');
INSERT INTO `orders` VALUES (49, 1, 1, '224f6849-9', 26.00, 0.00, 0, 2, NULL, NULL, '2025-03-08 18:40:56', '1:1:{容量=小份}');
INSERT INTO `orders` VALUES (50, 1, 1, 'd53ba191-0', 88.00, 0.00, 0, 2, NULL, NULL, '2025-03-08 19:42:44', '1:1:{};2:2:{容量=小份, 饮用=热饮}');
INSERT INTO `orders` VALUES (51, 6, 1, '1a66f925-b', 120.00, 0.00, 1, 2, NULL, NULL, '2025-03-08 20:01:50', '1:4:{辅料=加椰果}');
INSERT INTO `orders` VALUES (52, 6, 1, 'f4727863-0', 23.80, 0.00, 1, 2, NULL, NULL, '2025-03-08 20:03:53', '1:1:{}');
INSERT INTO `orders` VALUES (53, 6, 1, '7bb58ed0-6', 124.00, 0.00, 0, 2, NULL, NULL, '2025-03-08 20:16:52', '1:2:{};7:2:{甜度=正常糖, 辅料=加椰果}');

-- ----------------------------
-- Table structure for point_exchanges
-- ----------------------------
DROP TABLE IF EXISTS `point_exchanges`;
CREATE TABLE `point_exchanges`  (
  `exchange_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `product_id` int NULL DEFAULT NULL,
  `points_used` int NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '1:已兑换 2:已使用',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`exchange_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `point_exchanges_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `point_exchanges_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `point_products` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of point_exchanges
-- ----------------------------
INSERT INTO `point_exchanges` VALUES (1, 1, 1, NULL, 0, '2025-02-10 13:40:41');
INSERT INTO `point_exchanges` VALUES (2, 1, 4, NULL, 0, '2025-02-10 15:06:30');
INSERT INTO `point_exchanges` VALUES (3, 6, 1, NULL, 0, '2025-03-08 19:24:45');
INSERT INTO `point_exchanges` VALUES (4, 6, 2, NULL, 0, '2025-03-08 20:12:26');

-- ----------------------------
-- Table structure for point_products
-- ----------------------------
DROP TABLE IF EXISTS `point_products`;
CREATE TABLE `point_products`  (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `points_required` int NOT NULL,
  `stock` int NULL DEFAULT 0,
  `status` tinyint NULL DEFAULT 1 COMMENT '1:上架 0:下架',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of point_products
-- ----------------------------
INSERT INTO `point_products` VALUES (1, '10元代金券', 10, 100, 1);
INSERT INTO `point_products` VALUES (2, '20元代金券', 20, 100, 1);
INSERT INTO `point_products` VALUES (3, '50元代金券', 30, 50, 1);
INSERT INTO `point_products` VALUES (4, '兑换冰摩卡', 20, 100, 1);
INSERT INTO `point_products` VALUES (5, '兑换招牌奶茶', 10, 100, 1);
INSERT INTO `point_products` VALUES (6, '兑换芝士奶盖', 10, 100, 1);
INSERT INTO `point_products` VALUES (7, '兑换柠檬茶', 10, 100, 1);
INSERT INTO `point_products` VALUES (8, '兑换抹茶拿铁', 10, 150, 1);
INSERT INTO `point_products` VALUES (9, '兑换黑珍珠奶茶', 15, 200, 1);
INSERT INTO `point_products` VALUES (10, '兑换芒果沙冰', 8, 300, 1);

-- ----------------------------
-- Table structure for store_inventory
-- ----------------------------
DROP TABLE IF EXISTS `store_inventory`;
CREATE TABLE `store_inventory`  (
  `inventory_id` int NOT NULL AUTO_INCREMENT,
  `store_id` int NULL DEFAULT NULL,
  `dish_id` int NULL DEFAULT NULL,
  `stock` int NULL DEFAULT 0,
  PRIMARY KEY (`inventory_id`) USING BTREE,
  INDEX `store_id`(`store_id` ASC) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `store_inventory_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `store_inventory_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_inventory
-- ----------------------------
INSERT INTO `store_inventory` VALUES (1, 1, 1, 100);
INSERT INTO `store_inventory` VALUES (2, 1, 2, 100);
INSERT INTO `store_inventory` VALUES (3, 2, 1, 100);
INSERT INTO `store_inventory` VALUES (4, 2, 2, 100);
INSERT INTO `store_inventory` VALUES (5, 3, 1, 100);

-- ----------------------------
-- Table structure for stores
-- ----------------------------
DROP TABLE IF EXISTS `stores`;
CREATE TABLE `stores`  (
  `store_id` int NOT NULL AUTO_INCREMENT,
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `business_hours` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '1:营业中 0:休息中',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`store_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stores
-- ----------------------------
INSERT INTO `stores` VALUES (1, '快乐餐厅北京店', '北京市朝阳区朝阳门大街1号', '010-12345678', '09:00-22:00', 1, '2025-01-28 21:40:13');
INSERT INTO `stores` VALUES (2, '快乐餐厅上海店', '上海市浦东新区陆家嘴1号', '021-12345678', '09:00-22:00', 1, '2025-01-28 21:40:13');
INSERT INTO `stores` VALUES (3, '快乐餐厅广州店', '广州市天河区天河路1号', '020-12345678', '09:00-22:00', 1, '2025-01-28 21:40:13');

-- ----------------------------
-- Table structure for user_coupons
-- ----------------------------
DROP TABLE IF EXISTS `user_coupons`;
CREATE TABLE `user_coupons`  (
  `user_coupon_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `coupon_id` int NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '1:未使用 2:已使用 3:已过期',
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_coupon_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `coupon_id`(`coupon_id` ASC) USING BTREE,
  CONSTRAINT `user_coupons_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_coupons_ibfk_2` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`coupon_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_coupons
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `points` int NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `gener` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '021', '123', '张三', NULL, 2, '2025-01-28 21:40:13', '2025-06-07 14:38:13', '女');
INSERT INTO `users` VALUES (2, '13800138001', '123456', '李四', NULL, 300, '2025-01-28 21:40:13', '2025-02-11 13:47:31', '女');
INSERT INTO `users` VALUES (3, '13800138002', '123456', '王五', NULL, 500, '2025-01-28 21:40:13', '2025-02-11 13:47:36', '女');
INSERT INTO `users` VALUES (6, '13269592073', 'qq', 'qq', 'ic_avatar', 1, '2025-03-05 20:36:28', '2025-03-08 20:12:26', NULL);

-- ----------------------------
-- Table structure for reviews
-- ----------------------------
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews`  (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `user_id` int NOT NULL,
  `store_id` int NOT NULL,
  `rating` int NOT NULL,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`) USING BTREE,
  UNIQUE INDEX `uk_order_id`(`order_id`) USING BTREE COMMENT '一个订单只能评价一次',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_store_id`(`store_id` ASC) USING BTREE,
  CONSTRAINT `fk_reviews_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reviews_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reviews_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
