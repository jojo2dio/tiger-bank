package org.zoo.sysuser.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户数据传输对象（用于各层之间数据传递）
 */
@Data
@Schema(description = "用户数据传输对象")
public class SysUserDTO {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    private String role;
}
