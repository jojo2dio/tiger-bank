package org.zoo.repayment.facade;

import org.zoo.repayment.model.RepaymentDTO;
import org.zoo.repayment.model.RepaymentVO;
import java.math.BigDecimal;
import java.util.List;

/**
 * 还款记录服务接口
 */
public interface RepaymentService {
    /**
     * 创建还款记录并更新贷款剩余金额
     */
    void createRepayment(RepaymentDTO repaymentDTO, Long operatorId);

    /**
     * 根据ID查询还款记录
     */
    RepaymentVO getById(Long id);

    /**
     * 根据贷款ID查询还款记录列表
     */
    List<RepaymentVO> listByLoanId(Long loanId);

    /**
     * 根据企业ID查询还款记录列表
     */
    List<RepaymentVO> listByCustomerId(Long customerId);

    /**
     * 查询指定贷款的总还款金额
     */
    BigDecimal getTotalAmountByLoanId(Long loanId);

    /**
     * 查询指定贷款的总本金还款额
     */
    BigDecimal getTotalPrincipalByLoanId(Long loanId);

    List<RepaymentVO> listAll();

    List<RepaymentVO> page(int start, int limit, Long customerId, Long loanId, Integer repaymentType, Integer status);
}
