package org.zoo.sysuser.dal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    
    /**
     * 添加用户
     */
    int add(SysUser sysUser);
    
    /**
     * 根据ID删除用户
     */
    int deleteById(Long id);
    
    /**
     * 更新用户信息
     */
    int update(SysUser sysUser);
    
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
    List<SysUser> page(@Param("start") int start, @Param("limit") int limit);
    
    /**
     * 获取用户总数
     */
    int count();
}
