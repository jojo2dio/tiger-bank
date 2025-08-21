package org.zoo.loan.dal;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 贷款实体类（与数据库表对应）
 */
@Data
public class Loan {
    private Long id;
    private String productName; // 产品名称
    private Long customerId; // 企业ID
    private BigDecimal amount; // 贷款总金额(元)
    private BigDecimal remainingAmount; // 剩余未还金额(元)
    private BigDecimal interestRate; // 年利率(%)
    private Integer term; // 贷款期限(月)
    private LocalDate grantDate; // 放款日期
    private LocalDate dueDate; // 到期日期
    private Integer status; // 状态：0-待审批，1-已放款，2-已结清，3-已逾期，4-审批拒绝
    private Long approvalUserId; // 审批人ID
    private LocalDateTime approvalTime; // 审批时间
    private String approvalRemark; // 审批备注
    private Long createUserId; // 创建人ID
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
