package org.zoo.loan.facade;

import org.zoo.loan.model.LoanApprovalDTO;
import org.zoo.loan.model.LoanDTO;
import org.zoo.loan.model.LoanVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 贷款服务接口
 */
public interface LoanService {
    /**
     * 新增贷款
     */
    void add(LoanDTO loanDTO, Long createUserId);

    /**
     * 更新贷款
     */
    void update(LoanDTO loanDTO);

    /**
     * 根据ID删除贷款
     */
    void deleteById(Long id);

    /**
     * 根据ID查询贷款
     */
    LoanVO getById(Long id);

    /**
     * 根据企业ID查询贷款列表
     */
    List<LoanVO> listByCustomerId(Long customerId);

    /**
     * 查询所有贷款
     */
    List<LoanVO> listAll();

    /**
     * 分页查询贷款
     */
    List<LoanVO> page(int start, int limit);

    /**
     * 审批贷款
     */
    void approve(LoanApprovalDTO approvalDTO, Long approvalUserId);

    void updateRemainingAmount(Long loanId, BigDecimal amount);
}
