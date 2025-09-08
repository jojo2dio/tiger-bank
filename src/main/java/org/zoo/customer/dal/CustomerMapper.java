package org.zoo.customer.dal;

import org.apache.ibatis.annotations.Mapper;
import org.zoo.customer.dal.Customer;
import org.zoo.customer.model.CustomerVO;

import java.util.List;

/**
 * 客户Mapper接口
 */
@Mapper
public interface CustomerMapper {
    /**
     * 新增客户
     */
    int insert(Customer customer);

    /**
     * 更新客户
     */
    int update(Customer customer);

    /**
     * 根据ID删除客户
     */
    int deleteById(Long id);

    /**
     * 根据ID查询客户
     */
    Customer selectById(Long id);

    /**
     * 根据统一社会信用代码查询客户
     */
    Customer selectByCreditCode(String creditCode);

    /**
     * 查询所有客户
     */
    List<Customer> selectAll();

    /**
     * 分页查询客户
     */
    List<Customer> selectByPage(int start, int limit);

    /**
     * 查询总记录数
     */
    int selectTotalCount();

    List<CustomerVO> selectByConditions(String creditCode, String enterpriseName);
}
