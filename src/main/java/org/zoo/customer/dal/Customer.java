package org.zoo.customer.dal;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户实体类（与数据库表对应）
 */
@Data
public class Customer {
    private Long id;
    private String enterpriseName; // 企业名称
    private String creditCode; // 统一社会信用代码
    private String registerAddress; // 注册地址
    private BigDecimal registeredCapital; // 注册资本(万元)
    private String businessScope; // 营业范围
    private String legalPerson; // 法定代表人
    private String contactPhone; // 联系电话
    private Integer status; // 状态：0-禁用，1-正常
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUserId; // 创建人ID
}
