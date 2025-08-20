package org.zoo.sysuser.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    private String role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
