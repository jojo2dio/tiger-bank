package org.zoo.loan.facade.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zoo.common.exception.ServiceException;
import org.zoo.customer.dal.Customer;
import org.zoo.customer.dal.CustomerMapper;
import org.zoo.loan.dal.Loan;
import org.zoo.loan.dal.LoanGrant;
import org.zoo.loan.dal.LoanGrantMapper;
import org.zoo.loan.dal.LoanMapper;
import org.zoo.loan.facade.LoanGrantService;
import org.zoo.loan.model.LoanGrantDTO;
import org.zoo.loan.model.LoanGrantVO;
import org.zoo.sysuser.dal.SysUser;
import org.zoo.sysuser.dal.SysUserMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 放款记录服务实现类
 */
@Service
public class LoanGrantServiceImpl implements LoanGrantService {

    @Resource
    private LoanGrantMapper loanGrantMapper;
    
    @Resource
    private LoanMapper loanMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional
    public void createGrantRecord(LoanGrantDTO grantDTO, Long operatorId) {
        // 验证贷款是否存在
        Loan loan = loanMapper.selectById(grantDTO.getLoanId());
        if (loan == null) {
            throw new ServiceException("贷款项目不存在");
        }
        
        // 验证贷款状态是否为已审批待放款
        if (loan.getStatus() != 1) {
            throw new ServiceException("只有已放款状态的贷款才能创建放款记录");
        }
        
        // 验证放款金额是否合法
        if (grantDTO.getGrantAmount().compareTo(loan.getAmount()) > 0) {
            throw new ServiceException("放款金额不能超过贷款总金额");
        }
        
        // DTO转换为Entity
        LoanGrant loanGrant = new LoanGrant();
        BeanUtils.copyProperties(grantDTO, loanGrant);
        loanGrant.setCustomerId(loan.getCustomerId());
        loanGrant.setOperatorId(operatorId);
        loanGrant.setGrantTime(LocalDateTime.now());
        loanGrant.setStatus(1); // 默认放款成功
        loanGrant.setCreateTime(LocalDateTime.now());
        
        loanGrantMapper.insert(loanGrant);
    }

    @Override
    public LoanGrantVO getById(Long id) {
        LoanGrant loanGrant = loanGrantMapper.selectById(id);
        if (loanGrant == null) {
            return null;
        }
        return convertToVO(loanGrant);
    }

    @Override
    public List<LoanGrantVO> listByLoanId(Long loanId) {
        List<LoanGrant> grantList = loanGrantMapper.selectByLoanId(loanId);
        return convertToVOList(grantList);
    }

    @Override
    public List<LoanGrantVO> listByCustomerId(Long customerId) {
        List<LoanGrant> grantList = loanGrantMapper.selectByCustomerId(customerId);
        return convertToVOList(grantList);
    }

    /**
     * 转换为VO对象
     */
    private LoanGrantVO convertToVO(LoanGrant loanGrant) {
        LoanGrantVO vo = new LoanGrantVO();
        BeanUtils.copyProperties(loanGrant, vo);
        
        // 设置状态名称
        vo.setStatusName(loanGrant.getStatus() == 1 ? "成功" : "失败");
        
        // 查询贷款产品名称
        if (loanGrant.getLoanId() != null) {
            Loan loan = loanMapper.selectById(loanGrant.getLoanId());
            if (loan != null) {
                vo.setProductName(loan.getProductName());
            }
        }
        
        // 查询企业名称
        if (loanGrant.getCustomerId() != null) {
            Customer customer = customerMapper.selectById(loanGrant.getCustomerId());
            if (customer != null) {
                vo.setCustomerName(customer.getEnterpriseName());
            }
        }
        
        // 查询操作人姓名
        if (loanGrant.getOperatorId() != null) {
            SysUser operator = sysUserMapper.getById(loanGrant.getOperatorId());
            if (operator != null) {
                vo.setOperatorName(operator.getRealName());
            }
        }
        
        return vo;
    }

    /**
     * 转换为VO列表
     */
    private List<LoanGrantVO> convertToVOList(List<LoanGrant> grantList) {
        List<LoanGrantVO> voList = new ArrayList<>();
        for (LoanGrant grant : grantList) {
            voList.add(convertToVO(grant));
        }
        return voList;
    }
}
