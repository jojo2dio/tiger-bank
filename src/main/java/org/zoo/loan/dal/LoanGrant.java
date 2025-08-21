package org.zoo.loan.dal;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 放款记录实体类
 */
@Data
public class LoanGrant {
    private Long id;
    private Long loanId; // 贷款项目ID
    private Long customerId; // 企业ID
    private BigDecimal grantAmount; // 放款金额(元)
    private LocalDateTime grantTime; // 放款时间
    private Long operatorId; // 操作人ID
    private String remark; // 备注
    private Integer status; // 状态：0-失败，1-成功
    private LocalDateTime createTime; // 创建时间
}
