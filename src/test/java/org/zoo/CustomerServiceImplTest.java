package org.zoo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zoo.common.exception.ServiceException;
import org.zoo.customer.dal.Customer;
import org.zoo.customer.dal.CustomerMapper;
import org.zoo.customer.facade.impl.CustomerServiceImpl;
import org.zoo.customer.model.CustomerDTO;
import org.zoo.customer.model.CustomerVO;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void add_信用代码已存在_抛出异常() {
        CustomerDTO dto = new CustomerDTO();
        dto.setCreditCode("123456");
        when(customerMapper.selectByCreditCode("123456")).thenReturn(new Customer());

        assertThatThrownBy(() -> customerService.add(dto, 1L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("该统一社会信用代码已被注册");
    }

    @Test
    void add_正常情况_正确插入数据() {
        CustomerDTO dto = new CustomerDTO();
        dto.setEnterpriseName("测试企业");
        dto.setCreditCode("123456");
        when(customerMapper.selectByCreditCode("123456")).thenReturn(null);

        customerService.add(dto, 1L);

        verify(customerMapper).insert(argThat(customer -> 
            "测试企业".equals(customer.getEnterpriseName()) &&
                    customer.getCreateUserId().equals(1L) &&
                    customer.getCreateTime() != null
        ));
    }

    @Test
    void update_客户不存在_抛出异常() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1L);
        when(customerMapper.selectById(1L)).thenReturn(null);

        assertThatThrownBy(() -> customerService.update(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessage("客户不存在");
    }

    @Test
    void deleteById_客户不存在_抛出异常() {
        when(customerMapper.selectById(1L)).thenReturn(null);

        assertThatThrownBy(() -> customerService.deleteById(1L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("客户不存在");
    }

    @Test
    void listAll_返回正确VO列表() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEnterpriseName("企业A");
        customer.setStatus(1);
        when(customerMapper.selectAll()).thenReturn(Arrays.asList(customer));

        List<CustomerVO> result = customerService.listAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEnterpriseName()).isEqualTo("企业A");
        assertThat(result.get(0).getStatusName()).isEqualTo("正常"); // 状态转换验证
    }
}