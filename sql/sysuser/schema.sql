-- 创建数据库
CREATE DATABASE IF NOT EXISTS tiger_bank_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE tiger_bank_management;

-- 创建用户表
CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `username` varchar(50) NOT NULL COMMENT '用户名',
                            `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
                            `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
                            `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                            `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                            `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `role` varchar(20) NOT NULL COMMENT '角色：admin-管理员，staff-员工',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';