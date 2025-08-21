package org.zoo.repayment.dal;

import org.apache.ibatis.annotations.Mapper;
import java.math.BigDecimal;
import java.util.List;

/**
 * 还款记录Mapper接口
 */
@Mapper
public interface RepaymentRecordMapper {
    /**
     * 新增还款记录
     */
    int insert(RepaymentRecord repaymentRecord);

    /**
     * 根据ID查询还款记录
     */
    RepaymentRecord selectById(Long id);

    /**
     * 根据贷款ID查询还款记录列表
     */
    List<RepaymentRecord> selectByLoanId(Long loanId);

    /**
     * 根据企业ID查询还款记录列表
     */
    List<RepaymentRecord> selectByCustomerId(Long customerId);

    /**
     * 查询指定贷款的总还款金额
     */
    BigDecimal selectTotalAmountByLoanId(Long loanId);

    /**
     * 查询指定贷款的总本金还款额
     */
    BigDecimal selectTotalPrincipalByLoanId(Long loanId);
}
