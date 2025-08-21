package org.zoo.loan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 贷款审批DTO
 */
@Data
@ApiModel(description = "贷款审批DTO")
public class LoanApprovalDTO {
    @ApiModelProperty(value = "贷款项目ID", required = true)
    private Long loanId;
    
    @ApiModelProperty(value = "审批状态：1-已放款，4-审批拒绝", required = true)
    private Integer status;
    
    @ApiModelProperty(value = "审批备注")
    private String approvalRemark;
}
