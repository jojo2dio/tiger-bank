-- 创建客户信息表
CREATE TABLE `customer` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
                            `enterprise_name` varchar(100) NOT NULL COMMENT '企业名称',
                            `credit_code` varchar(50) NOT NULL COMMENT '统一社会信用代码',
                            `register_address` varchar(255) NOT NULL COMMENT '注册地址',
                            `registered_capital` decimal(15,2) DEFAULT NULL COMMENT '注册资本(万元)',
                            `business_scope` varchar(500) DEFAULT NULL COMMENT '营业范围',
                            `legal_person` varchar(50) DEFAULT NULL COMMENT '法定代表人',
                            `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                            `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_credit_code` (`credit_code`) COMMENT '统一社会信用代码唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款客户信息表';

-- 插入模拟数据
INSERT INTO `customer` (`enterprise_name`, `credit_code`, `register_address`, `registered_capital`, `business_scope`, `legal_person`, `contact_phone`, `status`, `create_user_id`)
VALUES
    ('科技创新有限公司', '91110101MA001A1B2C', '北京市海淀区科技园区88号', 500.00, '软件开发、技术服务', '张明', '13900139001', 1, 1),
    ('未来贸易有限公司', '91310101MA002B2C3D', '上海市浦东新区贸易大道123号', 1000.00, '货物进出口、国内贸易', '李强', '13800138002', 1, 1),
    ('绿色农业发展有限公司', '91440101MA003C3D4E', '广东省广州市农业路45号', 800.00, '农产品种植、加工、销售', '王芳', '13700137003', 1, 2),
    ('城市建设工程有限公司', '91500101MA004D4E5F', '重庆市渝中区建设大道67号', 2000.00, '建筑工程施工、市政工程', '赵伟', '13600136004', 0, 2);
