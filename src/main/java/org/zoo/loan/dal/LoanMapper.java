package org.zoo.loan.dal;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 贷款Mapper接口
 */
@Mapper
public interface LoanMapper {
    /**
     * 新增贷款
     */
    int insert(Loan loan);

    /**
     * 更新贷款
     */
    int update(Loan loan);

    /**
     * 根据ID删除贷款
     */
    int deleteById(Long id);

    /**
     * 根据ID查询贷款
     */
    Loan selectById(Long id);

    /**
     * 根据企业ID查询贷款列表
     */
    List<Loan> selectByCustomerId(Long customerId);

    /**
     * 查询所有贷款
     */
    List<Loan> selectAll();

    /**
     * 分页查询贷款
     */
    List<Loan> selectByPage(int start, int limit);

    /**
     * 查询总记录数
     */
    int selectTotalCount();

    /**
     * 更新贷款剩余金额
     */
    int updateRemainingAmount(Long id, BigDecimal remainingAmount);

    // 新增：根据条件查询贷款
    List<Loan> selectByCondition(Loan condition);

    // 建议新增：更新贷款状态的方法
    int updateStatusById(Long id, Integer status);

    List<Loan> selectByStatus(int status);
}
