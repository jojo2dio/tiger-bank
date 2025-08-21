package org.zoo.loan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 放款记录DTO
 */
@Data
@ApiModel(description = "放款记录DTO")
public class LoanGrantDTO {
    @ApiModelProperty(value = "贷款项目ID", required = true)
    private Long loanId;
    
    @ApiModelProperty(value = "放款金额(元)", required = true)
    private BigDecimal grantAmount;
    
    @ApiModelProperty(value = "备注")
    private String remark;
}
