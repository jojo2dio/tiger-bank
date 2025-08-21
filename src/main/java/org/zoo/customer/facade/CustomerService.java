package org.zoo.customer.facade;

import org.zoo.customer.model.CustomerDTO;
import org.zoo.customer.model.CustomerVO;

import java.util.List;

/**
 * 客户服务接口
 */
public interface CustomerService {
    /**
     * 新增客户
     */
    void add(CustomerDTO customerDTO, Long createUserId);

    /**
     * 更新客户
     */
    void update(CustomerDTO customerDTO);

    /**
     * 根据ID删除客户
     */
    void deleteById(Long id);

    /**
     * 根据ID查询客户
     */
    CustomerVO getById(Long id);

    /**
     * 根据统一社会信用代码查询客户
     */
    CustomerVO getByCreditCode(String creditCode);

    /**
     * 查询所有客户
     */
    List<CustomerVO> listAll();

    /**
     * 分页查询客户
     */
    List<CustomerVO> page(int start, int limit);
}
