package org.zoo.sysuser.facade.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zoo.common.util.JwtUtils;
import org.zoo.common.util.PasswordUtil;
import org.zoo.sysuser.dal.SysUser;
import org.zoo.sysuser.dal.SysUserMapper;
import org.zoo.sysuser.facade.SysUserService;
import org.zoo.common.exception.ServiceException;
import org.zoo.sysuser.model.LoginVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public LoginVO login(String username, String password) {
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
        // 判断: 判断是否存在这个员工, 如果存在, 组装登录成功信息

        log.info("登录成功, 员工信息: {}", user);
        //生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String jwt = JwtUtils.generateToken(claims);

        return new LoginVO(user.getId(), user.getUsername(), user.getUsername(), jwt);
    }
}
