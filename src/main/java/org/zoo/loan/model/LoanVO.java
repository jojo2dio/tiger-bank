package org.zoo.loan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 贷款VO（用于返回给前端）
 */
@Data
@ApiModel(description = "贷款信息VO")
public class LoanVO {
    @ApiModelProperty(value = "贷款项目ID")
    private Long id;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "企业ID")
    private Long customerId;

    @ApiModelProperty(value = "企业名称")
    private String customerName; // 关联客户表查询得到

    @ApiModelProperty(value = "贷款总金额(元)")
    private BigDecimal amount;

    @ApiModelProperty(value = "剩余未还金额(元)")
    private BigDecimal remainingAmount;

    @ApiModelProperty(value = "年利率(%)")
    private BigDecimal interestRate;

    @ApiModelProperty(value = "贷款期限(月)")
    private Integer term;

    @ApiModelProperty(value = "放款日期")
    private LocalDate grantDate;

    @ApiModelProperty(value = "到期日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "状态：0-待审批，1-已放款，2-已结清，3-已逾期，4-审批拒绝")
    private Integer status;

    @ApiModelProperty(value = "状态名称")
    private String statusName; // 状态中文名称

    @ApiModelProperty(value = "审批人ID")
    private Long approvalUserId;

    @ApiModelProperty(value = "审批人姓名")
    private String approvalUserName; // 关联用户表查询得到

    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approvalTime;

    @ApiModelProperty(value = "审批备注")
    private String approvalRemark;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName; // 关联用户表查询得到

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
