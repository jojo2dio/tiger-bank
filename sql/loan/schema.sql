-- 创建贷款信息表
CREATE TABLE `loan` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '贷款项目ID',
                        `product_name` varchar(100) NOT NULL COMMENT '产品名称',
                        `customer_id` bigint NOT NULL COMMENT '企业ID（关联customer表）',
                        `amount` decimal(16,2) NOT NULL COMMENT '贷款总金额(元)',
                        `remaining_amount` decimal(16,2) NOT NULL COMMENT '剩余未还金额(元)',
                        `interest_rate` decimal(5,2) NOT NULL COMMENT '年利率(%)',
                        `term` int NOT NULL COMMENT '贷款期限(月)',
                        `grant_date` date NOT NULL COMMENT '放款日期',
                        `due_date` date NOT NULL COMMENT '到期日期',
                        `status` tinyint NOT NULL COMMENT '状态：0-待审批，1-已放款，2-已结清，3-已逾期，4-审批拒绝',
                        `approval_user_id` bigint DEFAULT NULL COMMENT '审批人ID',
                        `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
                        `approval_remark` varchar(500) DEFAULT NULL COMMENT '审批备注',
                        `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_customer_id` (`customer_id`) COMMENT '企业ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款信息表';

-- 插入模拟数据
INSERT INTO `loan` (`product_name`, `customer_id`, `amount`, `remaining_amount`, `interest_rate`, `term`, `grant_date`, `due_date`, `status`, `approval_user_id`, `approval_time`, `approval_remark`, `create_user_id`)
VALUES
    ('经营贷', 1, 500000.00, 500000.00, 4.35, 12, '2025-01-15', '2026-01-15', 0, NULL, NULL, NULL, 2),
    ('流动资金贷', 2, 1000000.00, 800000.00, 4.50, 24, '2024-10-20', '2026-10-20', 1, 1, '2024-10-22 10:30:00', '同意放款', 2),
    ('设备采购贷', 3, 800000.00, 800000.00, 4.20, 36, '2025-02-05', '2028-02-05', 0, NULL, NULL, NULL, 2),
    ('固定资产贷', 1, 2000000.00, 0.00, 4.80, 60, '2022-05-10', '2027-05-10', 2, 1, '2022-05-15 14:20:00', '同意放款', 1);

-- 创建放款记录表
CREATE TABLE `loan_grant` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '放款记录ID',
                              `loan_id` bigint NOT NULL COMMENT '贷款项目ID（关联loan表）',
                              `customer_id` bigint NOT NULL COMMENT '企业ID（关联customer表）',
                              `grant_amount` decimal(16,2) NOT NULL COMMENT '放款金额(元)',
                              `grant_time` datetime NOT NULL COMMENT '放款时间',
                              `operator_id` bigint NOT NULL COMMENT '操作人ID',
                              `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                              `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_loan_id` (`loan_id`) COMMENT '贷款ID索引',
                              KEY `idx_customer_id` (`customer_id`) COMMENT '企业ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款放款记录表';

-- 插入模拟数据（对应之前的贷款数据）
INSERT INTO `loan_grant` (`loan_id`, `customer_id`, `grant_amount`, `grant_time`, `operator_id`, `remark`, `status`)
VALUES
    (2, 2, 1000000.00, '2024-10-22 15:30:00', 1, '首次放款', 1),
    (4, 1, 2000000.00, '2022-05-16 09:45:00', 1, '固定资产贷款放款', 1);
