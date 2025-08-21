-- 创建还款记录表
CREATE TABLE `repayment_record` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '还款记录ID',
                                    `customer_id` bigint NOT NULL COMMENT '企业ID（关联customer表）',
                                    `loan_id` bigint NOT NULL COMMENT '贷款项目ID（关联loan表）',
                                    `repayment_amount` decimal(16,2) NOT NULL COMMENT '还款金额(元)',
                                    `principal` decimal(16,2) DEFAULT NULL COMMENT '本金(元)',
                                    `interest` decimal(16,2) DEFAULT NULL COMMENT '利息(元)',
                                    `repayment_type` tinyint DEFAULT 1 COMMENT '还款类型：1-正常还款，2-提前还款，3-逾期还款',
                                    `repayment_time` datetime NOT NULL COMMENT '还款时间',
                                    `operator_id` bigint NOT NULL COMMENT '操作人ID',
                                    `remark` varchar(500) DEFAULT NULL COMMENT '还款备注',
                                    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    PRIMARY KEY (`id`),
                                    KEY `idx_loan_id` (`loan_id`) COMMENT '贷款ID索引',
                                    KEY `idx_customer_id` (`customer_id`) COMMENT '企业ID索引',
                                    KEY `idx_repayment_time` (`repayment_time`) COMMENT '还款时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='贷款还款记录表';

-- 插入测试数据
INSERT INTO `repayment_record` (
    `customer_id`, `loan_id`, `repayment_amount`, `principal`, `interest`,
    `repayment_type`, `repayment_time`, `operator_id`, `remark`, `status`
) VALUES
      (2, 2, 200000.00, 180000.00, 20000.00, 1, '2024-11-20 10:15:00', 2, '按月还款', 1),
      (1, 4, 500000.00, 450000.00, 50000.00, 1, '2023-05-10 09:30:00', 2, '季度还款', 1),
      (1, 4, 500000.00, 450000.00, 50000.00, 1, '2023-11-10 14:20:00', 2, '季度还款', 1),
      (1, 4, 1000000.00, 900000.00, 100000.00, 1, '2024-05-10 11:05:00', 2, '最终还款', 1);
