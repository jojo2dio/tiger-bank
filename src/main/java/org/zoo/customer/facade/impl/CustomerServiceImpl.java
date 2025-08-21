package org.zoo.customer.facade.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zoo.common.exception.ServiceException;
import org.zoo.customer.dal.Customer;
import org.zoo.customer.dal.CustomerMapper;
import org.zoo.customer.facade.CustomerService;
import org.zoo.customer.model.CustomerDTO;
import org.zoo.customer.model.CustomerVO;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户服务实现类
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public void add(CustomerDTO customerDTO, Long createUserId) {
        // 检查统一社会信用代码是否已存在
        Customer existingCustomer = customerMapper.selectByCreditCode(customerDTO.getCreditCode());
        if (existingCustomer != null) {
            throw new ServiceException("该统一社会信用代码已被注册");
        }

        // 检查客户是否已存在
        Customer customer = customerMapper.selectById(customerDTO.getId());
        if (customer != null) {
            throw new ServiceException("客户已存在");
        }

        // DTO转换为Entity
        customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customer.setCreateUserId(createUserId);
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());

        customerMapper.insert(customer);
    }

    @Override
    public void update(CustomerDTO customerDTO) {
        // 检查客户是否存在
        Customer existingCustomer = customerMapper.selectById(customerDTO.getId());
        if (existingCustomer == null) {
            throw new ServiceException("客户不存在");
        }

        // 检查统一社会信用代码是否被其他客户使用
        if (!existingCustomer.getCreditCode().equals(customerDTO.getCreditCode())) {
            Customer customerByCreditCode = customerMapper.selectByCreditCode(customerDTO.getCreditCode());
            if (customerByCreditCode != null) {
                throw new ServiceException("该统一社会信用代码已被注册");
            }
        }

        // DTO转换为Entity
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customer.setUpdateTime(LocalDateTime.now());

        customerMapper.update(customer);
    }

    @Override
    public void deleteById(Long id) {
        // 检查客户是否存在
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new ServiceException("客户不存在");
        }

        customerMapper.deleteById(id);
    }

    @Override
    public CustomerVO getById(Long id) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            return null;
        }
        return convertToVO(customer);
    }

    @Override
    public CustomerVO getByCreditCode(String creditCode) {
        Customer customer = customerMapper.selectByCreditCode(creditCode);
        if (customer == null) {
            return null;
        }
        return convertToVO(customer);
    }

    @Override
    public List<CustomerVO> listAll() {
        List<Customer> customers = customerMapper.selectAll();
        return convertToVOList(customers);
    }

    @Override
    public List<CustomerVO> page(int start, int limit) {
        List<Customer> customers = customerMapper.selectByPage(start, limit);
        return convertToVOList(customers);
    }

    /**
     * 将Entity转换为VO
     */
    private CustomerVO convertToVO(Customer customer) {
        CustomerVO vo = new CustomerVO();
        BeanUtils.copyProperties(customer, vo);

        // 设置状态中文名称
        if (customer.getStatus() != null) {
            vo.setStatusName(customer.getStatus() == 1 ? "正常" : "禁用");
        }

        return vo;
    }

    /**
     * 将Entity列表转换为VO列表
     */
    private List<CustomerVO> convertToVOList(List<Customer> customers) {
        List<CustomerVO> voList = new ArrayList<>();
        for (Customer customer : customers) {
            voList.add(convertToVO(customer));
        }
        return voList;
    }
}
