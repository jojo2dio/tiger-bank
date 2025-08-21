package org.zoo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zoo.common.exception.ServiceException;
import org.zoo.common.util.PasswordUtil;
import org.zoo.sysuser.dal.SysUser;
import org.zoo.sysuser.dal.SysUserMapper;
import org.zoo.sysuser.facade.impl.SysUserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SysUserServiceImplTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @InjectMocks
    private SysUserServiceImpl sysUserService;

    @Test
    void add_用户名已存在_抛出异常() {
        // 准备数据
        SysUser user = new SysUser();
        user.setUsername("test");
        when(sysUserMapper.getByUsername("test")).thenReturn(new SysUser());

        // 验证异常
        assertThatThrownBy(() -> sysUserService.add(user))
                .isInstanceOf(ServiceException.class)
                .hasMessage("用户名已存在");
        verify(sysUserMapper, never()).add(any());
    }

    @Test
    void add_正常情况_密码加密存储() {
        // 准备数据
        SysUser user = new SysUser();
        user.setUsername("test");
        user.setPassword("123456");
        when(sysUserMapper.getByUsername("test")).thenReturn(null);

        // 执行方法
        sysUserService.add(user);

        // 验证密码加密且调用mapper
        verify(sysUserMapper).add(argThat(u -> 
            PasswordUtil.verify("123456", u.getPassword())
        ));
    }

    @Test
    void login_用户名不存在_抛出异常() {
        when(sysUserMapper.getByUsername("test")).thenReturn(null);

        assertThatThrownBy(() -> sysUserService.login("test", "123"))
                .isInstanceOf(ServiceException.class)
                .hasMessage("用户名或密码错误");
    }

    @Test
    void login_密码错误_抛出异常() {
        SysUser user = new SysUser();
        user.setUsername("test");
        user.setPassword(PasswordUtil.encrypt("123"));
        user.setStatus(1);
        when(sysUserMapper.getByUsername("test")).thenReturn(user);

        assertThatThrownBy(() -> sysUserService.login("test", "456"))
                .isInstanceOf(ServiceException.class)
                .hasMessage("用户名或密码错误");
    }

    @Test
    void page_分页查询_返回正确数据() {
        // 模拟数据
        SysUser user1 = new SysUser();
        user1.setId(1L);
        user1.setUsername("user1");
        SysUser user2 = new SysUser();
        user2.setId(2L);
        user2.setUsername("user2");
        when(sysUserMapper.page(0, 10)).thenReturn(Arrays.asList(user1, user2));
        when(sysUserMapper.count()).thenReturn(2);

        // 执行方法
        Map<String, Object> result = sysUserService.page(0, 10);

        // 验证结果
        assertThat(result).containsEntry("total", 2);
        assertThat(((List<SysUser>) result.get("list"))).hasSize(2);
        // 验证密码脱敏
        ((List<SysUser>) result.get("list")).forEach(u -> assertThat(u.getPassword()).isNull());
    }
}