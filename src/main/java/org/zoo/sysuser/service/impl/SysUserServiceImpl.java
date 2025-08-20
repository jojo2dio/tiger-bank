package org.zoo.sysuser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zoo.common.util.PasswordUtil;
import org.zoo.sysuser.entity.SysUser;
import org.zoo.sysuser.mapper.SysUserMapper;
import org.zoo.sysuser.service.SysUserService;
import org.zoo.common.exception.ServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void add(SysUser sysUser) {
        // 检查用户名是否已存在
        SysUser existingUser = sysUserMapper.getByUsername(sysUser.getUsername());
        if (existingUser != null) {
            throw new ServiceException("用户名已存在");
        }

        // 密码加密
        sysUser.setPassword(PasswordUtil.encrypt(sysUser.getPassword()));
        sysUserMapper.add(sysUser);
    }

    @Override
    public void deleteById(Long id) {
        SysUser user = sysUserMapper.getById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        sysUserMapper.deleteById(id);
    }

    @Override
    public void update(SysUser sysUser) {
        SysUser existingUser = sysUserMapper.getById(sysUser.getId());
        if (existingUser == null) {
            throw new ServiceException("用户不存在");
        }

        // 如果修改了用户名，检查新用户名是否已存在
        if (!existingUser.getUsername().equals(sysUser.getUsername())) {
            SysUser userWithSameUsername = sysUserMapper.getByUsername(sysUser.getUsername());
            if (userWithSameUsername != null) {
                throw new ServiceException("用户名已存在");
            }
        }

        // 如果修改了密码，则加密处理
        if (sysUser.getPassword() != null && !sysUser.getPassword().equals(existingUser.getPassword())) {
            sysUser.setPassword(PasswordUtil.encrypt(sysUser.getPassword()));
        } else {
            // 避免覆盖原密码
            sysUser.setPassword(null);
        }

        sysUserMapper.update(sysUser);
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = sysUserMapper.getById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        // 密码脱敏
        user.setPassword(null);
        return user;
    }

    @Override
    public SysUser getByUsername(String username) {
        SysUser user = sysUserMapper.getByUsername(username);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        // 密码脱敏
        user.setPassword(null);
        return user;
    }

    @Override
    public List<SysUser> list() {
        List<SysUser> users = sysUserMapper.list();
        // 密码脱敏
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    @Override
    public Map<String, Object> page(int start, int limit) {
        List<SysUser> users = sysUserMapper.page(start, limit);
        // 密码脱敏
        users.forEach(user -> user.setPassword(null));

        int total = sysUserMapper.count();
        Map<String, Object> result = new HashMap<>();
        result.put("list", users);
        result.put("total", total);
        return result;
    }

    @Override
    public SysUser login(String username, String password) {
        SysUser user = sysUserMapper.getByUsername(username);
        if (user == null) {
            throw new ServiceException("用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            throw new ServiceException("账号已禁用，请联系管理员");
        }

        if (!PasswordUtil.verify(password, user.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }

        // 密码脱敏
        user.setPassword(null);
        return user;
    }
}
