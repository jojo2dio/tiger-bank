package org.zoo.loan.facade;

import org.zoo.loan.model.LoanGrantDTO;
import org.zoo.loan.model.LoanGrantVO;
import java.util.List;

/**
 * 放款记录服务接口
 */
public interface LoanGrantService {
    /**
     * 创建放款记录
     */
    void createGrantRecord(LoanGrantDTO grantDTO, Long operatorId);

    /**
     * 根据ID查询放款记录
     */
    LoanGrantVO getById(Long id);

    /**
     * 根据贷款ID查询放款记录列表
     */
    List<LoanGrantVO> listByLoanId(Long loanId);

    /**
     * 根据企业ID查询放款记录列表
     */
    List<LoanGrantVO> listByCustomerId(Long customerId);

    List<LoanGrantVO> page(int start, int limit);
}
