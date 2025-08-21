package org.zoo.loan.dal;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 放款记录Mapper接口
 */
@Mapper
public interface LoanGrantMapper {
    /**
     * 新增放款记录
     */
    int insert(LoanGrant loanGrant);

    /**
     * 根据ID查询放款记录
     */
    LoanGrant selectById(Long id);

    /**
     * 根据贷款ID查询放款记录列表
     */
    List<LoanGrant> selectByLoanId(Long loanId);

    /**
     * 根据企业ID查询放款记录列表
     */
    List<LoanGrant> selectByCustomerId(Long customerId);
}
