package org.zoo.repayment.dal;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 还款记录实体类
 */
@Data
public class RepaymentRecord {
    private Long id;
    private Long customerId; // 企业ID
    private Long loanId; // 贷款项目ID
    private BigDecimal repaymentAmount; // 还款总金额(元)
    private BigDecimal principal; // 本金(元)
    private BigDecimal interest; // 利息(元)
    private Integer repaymentType; // 还款类型：1-正常还款，2-提前还款，3-逾期还款
    private LocalDateTime repaymentTime; // 还款时间
    private Long operatorId; // 操作人ID
    private String remark; // 还款备注
    private Integer status; // 状态：0-失败，1-成功
    private LocalDateTime createTime; // 创建时间
}
