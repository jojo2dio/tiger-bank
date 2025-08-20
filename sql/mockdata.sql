-- 插入模拟数据
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `status`, `role`)
VALUES
    ('admin', '689cbec3f37b021dc5e0c7e381d2b3651744d58328a3ab909de3d1e495b71a11', '系统管理员', '13800138000', 'admin@tigerbank.com', 1, 'admin'),
    ('staff1', '689cbec3f37b021dc5e0c7e381d2b3651744d58328a3ab909de3d1e495b71a11', '张三', '13800138001', 'zhangsan@tigerbank.com', 1, 'staff'),
    ('staff2', '689cbec3f37b021dc5e0c7e381d2b3651744d58328a3ab909de3d1e495b71a11', '李四', '13800138002', 'lisi@tigerbank.com', 1, 'staff');