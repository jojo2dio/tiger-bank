package org.zoo.sysuser.facade;

import org.zoo.sysuser.dal.SysUser;
import java.util.List;
import java.util.Map;

public interface SysUserService {
    
    /**
     * 添加用户
     */
    void add(SysUser sysUser);
    
    /**
     * 根据ID删除用户
     */
    void deleteById(Long id);
    
    /**
     * 更新用户信息
     */
    void update(SysUser sysUser);
    
    /**
     * 根据ID获取用户
     */
    SysUser getById(Long id);
    
    /**
     * 根据用户名获取用户
     */
    SysUser getByUsername(String username);
    
    /**
     * 获取所有用户列表
     */
    List<SysUser> list();
    
    /**
     * 分页获取用户列表
     */
    Map<String, Object> page(int start, int limit);
    
    /**
     * 用户登录
     */
    SysUser login(String username, String password);
}
