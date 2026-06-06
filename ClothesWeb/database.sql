-- ==========================================
-- 网上衣橱系统 数据库设计
-- 数据库：MySQL 9.2
-- 字符集：utf8mb4
-- ==========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS wardrobe DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE wardrobe;

-- ==========================================
-- 1. t_user 用户表
-- 存储前台用户和后台管理员
-- role: 1-管理员，2-普通用户
-- ==========================================
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    address VARCHAR(500) DEFAULT NULL COMMENT '地址',
    role TINYINT NOT NULL DEFAULT 2 COMMENT '角色：1-管理员，2-普通用户',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入默认管理员账号（密码：admin123，BCrypt加密）
-- 注意：如果这个密码无法登录，请先通过注册接口注册一个普通用户，
-- 然后在数据库中将该用户的 role 字段改为 1（管理员）
INSERT INTO t_user (username, password, phone, role) VALUES 
('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyW57V5.4J1m', '13800138000', 1);

-- ==========================================
-- 2. t_type 服装类型表
-- ==========================================
CREATE TABLE IF NOT EXISTS t_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '类型ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '类型名称',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服装类型表';

-- 插入默认服装类型
INSERT INTO t_type (name) VALUES 
('连衣裙'),
('T恤'),
('衬衫'),
('外套'),
('裤子'),
('裙子'),
('帽子'),
('鞋子');

-- ==========================================
-- 3. t_size 尺码表
-- 不同类型对应不同尺码
-- ==========================================
CREATE TABLE IF NOT EXISTS t_size (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '尺码ID',
    type_id BIGINT NOT NULL COMMENT '类型ID',
    name VARCHAR(20) NOT NULL COMMENT '尺码名称',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (type_id) REFERENCES t_type(id) ON DELETE CASCADE,
    INDEX idx_type_id (type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='尺码表';

-- 插入默认尺码数据
-- 连衣裙
INSERT INTO t_size (type_id, name) VALUES 
(1, 'S'), (1, 'M'), (1, 'L'), (1, 'XL');

-- T恤
INSERT INTO t_size (type_id, name) VALUES 
(2, 'S'), (2, 'M'), (2, 'L'), (2, 'XL'), (2, 'XXL');

-- 衬衫
INSERT INTO t_size (type_id, name) VALUES 
(3, 'S'), (3, 'M'), (3, 'L'), (3, 'XL');

-- 外套
INSERT INTO t_size (type_id, name) VALUES 
(4, 'S'), (4, 'M'), (4, 'L'), (4, 'XL'), (4, 'XXL');

-- 裤子
INSERT INTO t_size (type_id, name) VALUES 
(5, '26'), (5, '27'), (5, '28'), (5, '29'), (5, '30'), (5, '31'), (5, '32');

-- 裙子
INSERT INTO t_size (type_id, name) VALUES 
(6, 'S'), (6, 'M'), (6, 'L'), (6, 'XL');

-- 帽子
INSERT INTO t_size (type_id, name) VALUES 
(7, '均码');

-- 鞋子
INSERT INTO t_size (type_id, name) VALUES 
(8, '35'), (8, '36'), (8, '37'), (8, '38'), (8, '39'), (8, '40'), (8, '41'), (8, '42'), (8, '43');

-- ==========================================
-- 4. t_clothes 服装表
-- ==========================================
CREATE TABLE IF NOT EXISTS t_clothes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '服装ID',
    name VARCHAR(200) NOT NULL COMMENT '服装名称',
    type_id BIGINT NOT NULL COMMENT '类型ID',
    style VARCHAR(50) DEFAULT NULL COMMENT '风格',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    image_url VARCHAR(500) DEFAULT NULL COMMENT '图片URL',
    description TEXT DEFAULT NULL COMMENT '描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (type_id) REFERENCES t_type(id) ON DELETE CASCADE,
    INDEX idx_type_id (type_id),
    INDEX idx_status (status),
    INDEX idx_style (style)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服装表';

-- 插入示例服装数据
INSERT INTO t_clothes (name, type_id, style, price, description, status) VALUES 
('优雅雪纺连衣裙', 1, '优雅', 299.00, '轻盈雪纺材质，适合春夏穿着', 1),
('简约棉质T恤', 2, '简约', 99.00, '100%纯棉，舒适透气', 1),
('商务正装衬衫', 3, '商务', 199.00, '免烫面料，职场必备', 1),
('时尚牛仔外套', 4, '休闲', 399.00, '经典牛仔款式，百搭单品', 1),
('修身直筒裤', 5, '修身', 249.00, '显腿长，百搭裤型', 1),
('飘逸半身裙', 6, '甜美', 179.00, '飘逸裙摆，温柔气质', 1),
('时尚棒球帽', 7, '休闲', 59.00, '防晒遮阳，时尚百搭', 1),
('百搭小白鞋', 8, '简约', 259.00, '真皮材质，舒适耐穿', 1);

-- ==========================================
-- 5. t_cart 购物车表
-- ==========================================
CREATE TABLE IF NOT EXISTS t_cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    clothes_id BIGINT NOT NULL COMMENT '服装ID',
    clothes_size VARCHAR(20) NOT NULL COMMENT '服装尺码',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
    FOREIGN KEY (clothes_id) REFERENCES t_clothes(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_clothes_size (user_id, clothes_id, clothes_size),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- ==========================================
-- 6. t_order 订单表
-- status: 0-未支付，1-未发货，2-已发货，3-已收货
-- ==========================================
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    clothes_details TEXT NOT NULL COMMENT '订单商品详情（JSON格式）',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    address VARCHAR(500) NOT NULL COMMENT '收货地址',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-未支付，1-未发货，2-已发货，3-已收货',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';