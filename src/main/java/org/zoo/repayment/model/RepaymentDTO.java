package org.zoo.repayment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 还款记录DTO
 */
@Data
@ApiModel(description = "还款记录数据传输对象")
public class RepaymentDTO {
    @ApiModelProperty(value = "贷款项目ID", required = true)
    private Long loanId;
    
    @ApiModelProperty(value = "还款总金额(元)", required = true)
    private BigDecimal repaymentAmount;
    
    @ApiModelProperty(value = "本金(元)")
    private BigDecimal principal;
    
    @ApiModelProperty(value = "利息(元)")
    private BigDecimal interest;
    
    @ApiModelProperty(value = "还款类型：1-正常还款，2-提前还款，3-逾期还款，默认1")
    private Integer repaymentType = 1;
    
    @ApiModelProperty(value = "还款备注")
    private String remark;
}
