package org.zoo.repayment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 还款记录VO
 */
@Data
@ApiModel(description = "还款记录视图对象")
public class RepaymentVO {
    @ApiModelProperty(value = "还款记录ID")
    private Long id;
    
    @ApiModelProperty(value = "企业ID")
    private Long customerId;
    
    @ApiModelProperty(value = "企业名称")
    private String customerName; // 关联客户表
    
    @ApiModelProperty(value = "贷款项目ID")
    private Long loanId;
    
    @ApiModelProperty(value = "贷款产品名称")
    private String productName; // 关联贷款表
    
    @ApiModelProperty(value = "还款总金额(元)")
    private BigDecimal repaymentAmount;
    
    @ApiModelProperty(value = "本金(元)")
    private BigDecimal principal;
    
    @ApiModelProperty(value = "利息(元)")
    private BigDecimal interest;
    
    @ApiModelProperty(value = "还款类型：1-正常还款，2-提前还款，3-逾期还款")
    private Integer repaymentType;
    
    @ApiModelProperty(value = "还款类型名称")
    private String repaymentTypeName;
    
    @ApiModelProperty(value = "还款时间")
    private LocalDateTime repaymentTime;
    
    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;
    
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName; // 关联用户表
    
    @ApiModelProperty(value = "还款备注")
    private String remark;
    
    @ApiModelProperty(value = "状态：0-失败，1-成功")
    private Integer status;
    
    @ApiModelProperty(value = "状态名称")
    private String statusName;
    
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
