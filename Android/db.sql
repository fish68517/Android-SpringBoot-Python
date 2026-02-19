-- 创建数据库
CREATE DATABASE IF NOT EXISTS restaurant_chain DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE restaurant_chain;

-- 用户表
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    phone VARCHAR(11) NOT NULL UNIQUE,
    password VARCHAR(255),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    points INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 店铺表
CREATE TABLE stores (
    store_id INT PRIMARY KEY AUTO_INCREMENT,
    store_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    business_hours VARCHAR(100),
    status TINYINT DEFAULT 1 COMMENT '1:营业中 0:休息中',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 菜品分类表
CREATE TABLE dish_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '1:启用 0:禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 菜品表
CREATE TABLE dishes (
    dish_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT,
    dish_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    status TINYINT DEFAULT 1 COMMENT '1:上架 0:下架',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES dish_categories(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 店铺库存表
CREATE TABLE store_inventory (
    inventory_id INT PRIMARY KEY AUTO_INCREMENT,
    store_id INT,
    dish_id INT,
    stock INT DEFAULT 0,
    FOREIGN KEY (store_id) REFERENCES stores(store_id),
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 订单表
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    store_id INT,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    total_amount DECIMAL(10,2) NOT NULL,
    delivery_fee DECIMAL(10,2) DEFAULT 0,
    status TINYINT DEFAULT 0 COMMENT '0:待支付 1:已支付 2:配送中 3:已完成 4:已取消',
    order_type TINYINT COMMENT '1:堂食 2:外卖',
    delivery_address TEXT,
    payment_method TINYINT COMMENT '1:微信 2:支付宝',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (store_id) REFERENCES stores(store_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


order_id: 订单的唯一标识 ID，是主键。
user_id: 下单用户的 ID，与 users 表关联。
store_id: 订单所属的商铺 ID，与 stores 表关联。
order_no: 订单编号,是一个唯一的字符串标识。
total_amount: 订单的总金额。
delivery_fee: 订单的配送费用,默认为 0。
status: 订单的状态,取值为 0-4 分别表示待支付、已支付、配送中、已完成、已取消。
order_type: 订单类型,1 表示堂食,2 表示外卖。
delivery_address: 外卖订单的配送地址。
payment_method: 支付方式,1 表示微信支付,2 表示支付宝支付。
created_at: 订单创建时间,默认为当前时间戳。


-- 订单详情表
CREATE TABLE order_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    dish_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 优惠券表
CREATE TABLE coupons (
    coupon_id INT PRIMARY KEY AUTO_INCREMENT,
    coupon_name VARCHAR(100) NOT NULL,
    coupon_type TINYINT COMMENT '1:满减 2:折扣',
    min_amount DECIMAL(10,2) COMMENT '最低使用金额',
    discount_amount DECIMAL(10,2) COMMENT '优惠金额',
    discount_rate DECIMAL(3,2) COMMENT '折扣率',
    valid_days INT COMMENT '有效期天数',
    status TINYINT DEFAULT 1 COMMENT '1:有效 0:无效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户优惠券表
CREATE TABLE user_coupons (
    user_coupon_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    coupon_id INT,
    status TINYINT DEFAULT 1 COMMENT '1:未使用 2:已使用 3:已过期',
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 积分商品表
CREATE TABLE point_products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    points_required INT NOT NULL,
    stock INT DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '1:上架 0:下架'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 积分兑换记录表
CREATE TABLE point_exchanges (
    exchange_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    product_id INT,
    points_used INT NOT NULL,
    status TINYINT DEFAULT 1 COMMENT '1:已兑换 2:已使用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES point_products(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入示例数据
-- 用户数据
INSERT INTO users (phone, nickname) VALUES
('13800138000', '张三'),
('13800138001', '李四'),
('13800138002', '王五');

-- 店铺数据
INSERT INTO stores (store_name, address, phone, business_hours) VALUES
('快乐餐厅北京店', '北京市朝阳区朝阳门大街1号', '010-12345678', '09:00-22:00'),
('快乐餐厅上海店', '上海市浦东新区陆家嘴1号', '021-12345678', '09:00-22:00'),
('快乐餐厅广州店', '广州市天河区天河路1号', '020-12345678', '09:00-22:00');

-- 菜品分类数据
INSERT INTO dish_categories (category_name, sort_order) VALUES
('热销推荐', 1),
('主食', 2),
('小吃', 3),
('饮品', 4);

-- 菜品数据
INSERT INTO dishes (category_id, dish_name, price, description) VALUES
(1, '招牌炒饭', 28.00, '使用特制酱料炒制，搭配多种配料'),
(1, '红烧牛肉面', 32.00, '传统工艺，汤浓肉烂'),
(2, '扬州炒饭', 26.00, '配料丰富，口感绝佳'),
(3, '蒜蓉炒青菜', 16.00, '新鲜时蔬，清淡爽口'),
(4, '冰镇柠檬茶', 12.00, '清爽解腻');

-- 店铺库存数据
INSERT INTO store_inventory (store_id, dish_id, stock) VALUES
(1, 1, 100),
(1, 2, 100),
(2, 1, 100),
(2, 2, 100),
(3, 1, 100);

-- 优惠券数据
INSERT INTO coupons (coupon_name, coupon_type, min_amount, discount_amount, discount_rate, valid_days) VALUES
('满100减20券', 1, 100.00, 20.00, NULL, 30),
('85折优惠券', 2, 50.00, NULL, 0.85, 30);

-- 积分商品数据
INSERT INTO point_products (product_name, points_required, stock) VALUES
('10元代金券', 1000, 100),
('20元代金券', 1800, 100),
('50元代金券', 4000, 50);


-- 规格组表（如分量、辣度、甜度等）
CREATE TABLE spec_group (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '规格组名称，如：分量、辣度、甜度',
    category_id INT NOT NULL COMMENT '菜品分类ID',
    sort INT DEFAULT 0 COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES dish_categories(category_id)
);

-- 规格选项表（如半份、一份、微辣、中辣等）
CREATE TABLE spec_option (
    id INT PRIMARY KEY AUTO_INCREMENT,
    group_id INT NOT NULL COMMENT '规格组ID',
    name VARCHAR(50) NOT NULL COMMENT '规格选项名称',
    price_delta DECIMAL(10,2) DEFAULT 0.00 COMMENT '价格调整值（+/-）',
    sort INT DEFAULT 0 COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES spec_group(id)
);

-- 菜品规格关联表（用于关联菜品和规格组）
CREATE TABLE dish_spec (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dish_id INT NOT NULL COMMENT '菜品ID',
    group_id INT NOT NULL COMMENT '规格组ID',
    is_required BOOLEAN DEFAULT true COMMENT '是否必选',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id),
    FOREIGN KEY (group_id) REFERENCES spec_group(id)
);


-- 插入规格组数据
INSERT INTO spec_group (name, category_id) VALUES
-- 主食规格组
('分量', 2),
('配料', 2),
-- 小吃规格组
('分量', 3),
('辣度', 3),
('配料', 3),
-- 饮品规格组
('容量', 4),
('甜度', 4);

-- 插入规格选项数据
INSERT INTO spec_option (group_id, name, price_delta) VALUES
-- 主食分量选项
(1, '半份', -5.00),
(1, '一份', 0),
-- 主食配料选项
(2, '加葱花', 0),
(2, '加香菜', 0),
-- 小吃分量选项
(3, '半份', -4.00),
(3, '一份', 0),
-- 小吃辣度选项
(4, '微辣', 0),
(4, '中辣', 0),
(4, '麻辣', 0),
-- 小吃配料选项
(5, '加葱花', 0),
(5, '加香菜', 0),
-- 饮品容量选项
(6, '小杯', -2.00),
(6, '中杯', 0),
(6, '大杯', 3.00),
-- 饮品甜度选项
(7, '3分糖', 0),
(7, '半糖', 0),
(7, '全糖', 0),
(7, '额外加糖', 0);

-- 为菜品关联规格组
INSERT INTO dish_spec (dish_id, group_id, is_required) VALUES
-- 主食示例（假设dish_id=1是一个主食）
(1, 1, false),  -- 分量必选
(1, 2, false), -- 配料可选
-- 小吃示例（假设dish_id=2是一个小吃）
(2, 3, false),  -- 分量必选
(2, 4, false),  -- 辣度必选
(2, 5, false), -- 配料可选
-- 饮品示例（假设dish_id=3是一个饮品）
(3, 6, false),  -- 容量必选
(3, 7, false);  -- 甜度必选