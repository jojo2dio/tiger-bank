package org.zoo.customer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户VO（用于返回给前端）
 */
@Data
@Schema(description = "客户信息VO")
public class CustomerVO {
    @Schema(description = "客户ID")
    private Long id;

    @Schema(description = "企业名称")
    private String enterpriseName;

    @Schema(description = "统一社会信用代码")
    private String creditCode;

    @Schema(description = "注册地址")
    private String registerAddress;

    @Schema(description = "注册资本(万元)")
    private BigDecimal registeredCapital;

    @Schema(description = "营业范围")
    private String businessScope;

    @Schema(description = "法定代表人")
    private String legalPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "状态：0-禁用，1-正常")
    private Integer status;

    @Schema(description = "状态名称")
    private String statusName; // 额外字段，显示状态中文名称

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
