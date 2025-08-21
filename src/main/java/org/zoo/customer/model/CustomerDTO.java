package org.zoo.customer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户DTO（用于各层之间数据传输）
 */
@Data
@Schema(description = "客户信息DTO")
public class CustomerDTO {
    @Schema(description = "客户ID，新增时无需传入")
    private Long id;

    @Schema(description = "企业名称", required = true)
    private String enterpriseName;

    @Schema(description = "统一社会信用代码", required = true)
    private String creditCode;

    @Schema(description = "注册地址", required = true)
    private String registerAddress;

    @Schema(description = "注册资本(万元)")
    private BigDecimal registeredCapital;

    @Schema(description = "营业范围")
    private String businessScope;

    @Schema(description = "法定代表人")
    private String legalPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "状态：0-禁用，1-正常，默认1")
    private Integer status = 1;
}
