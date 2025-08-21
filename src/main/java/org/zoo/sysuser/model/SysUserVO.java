package org.zoo.sysuser.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象（用于控制器返回给前端）
 */
@Data
@Schema(description = "用户视图对象")
public class SysUserVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    private String role;
    private LocalDateTime createTime;
    
    // 不返回密码等敏感信息
}
