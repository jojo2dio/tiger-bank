package org.zoo.loan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 贷款DTO（用于各层之间数据传输）
 */
@Data
@ApiModel(description = "贷款信息DTO")
public class LoanDTO {
    @ApiModelProperty(value = "贷款项目ID，新增时无需传入")
    private Long id;

    @ApiModelProperty(value = "产品名称", required = true)
    private String productName;

    @ApiModelProperty(value = "企业ID", required = true)
    private Long customerId;

    @ApiModelProperty(value = "贷款总金额(元)", required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "年利率(%)", required = true)
    private BigDecimal interestRate;

    @ApiModelProperty(value = "贷款期限(月)", required = true)
    private Integer term;

    @ApiModelProperty(value = "放款日期")
    private LocalDate grantDate;

    @ApiModelProperty(value = "到期日期", required = true)
    private LocalDate dueDate;

    @ApiModelProperty(value = "状态：0-待审批，1-已放款，2-已结清，3-已逾期，4-审批拒绝，新增时默认为0")
    private Integer status = 0;

    @ApiModelProperty(value = "审批备注")
    private String approvalRemark;
}
