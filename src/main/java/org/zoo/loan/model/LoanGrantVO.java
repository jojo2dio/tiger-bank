package org.zoo.loan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 放款记录VO
 */
@Data
@ApiModel(description = "放款记录VO")
public class LoanGrantVO {
    @ApiModelProperty(value = "放款记录ID")
    private Long id;
    
    @ApiModelProperty(value = "贷款项目ID")
    private Long loanId;
    
    @ApiModelProperty(value = "贷款产品名称")
    private String productName;
    
    @ApiModelProperty(value = "企业ID")
    private Long customerId;
    
    @ApiModelProperty(value = "企业名称")
    private String customerName;
    
    @ApiModelProperty(value = "放款金额(元)")
    private BigDecimal grantAmount;
    
    @ApiModelProperty(value = "放款时间")
    private LocalDateTime grantTime;
    
    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;
    
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "状态：0-失败，1-成功")
    private Integer status;
    
    @ApiModelProperty(value = "状态名称")
    private String statusName;
    
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
