package org.zoo.repayment.facade.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zoo.common.exception.ServiceException;
import org.zoo.customer.dal.Customer;
import org.zoo.customer.dal.CustomerMapper;
import org.zoo.loan.dal.Loan;
import org.zoo.loan.dal.LoanMapper;
import org.zoo.loan.facade.LoanService;
import org.zoo.repayment.dal.RepaymentRecord;
import org.zoo.repayment.dal.RepaymentRecordMapper;
import org.zoo.repayment.facade.RepaymentService;
import org.zoo.repayment.model.RepaymentDTO;
import org.zoo.repayment.model.RepaymentVO;
import org.zoo.sysuser.dal.SysUser;
import org.zoo.sysuser.dal.SysUserMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 还款记录服务实现类
 */
@Service
public class RepaymentServiceImpl implements RepaymentService {

    @Resource
    private RepaymentRecordMapper repaymentRecordMapper;
    
    @Resource
    private LoanMapper loanMapper;
    
    @Resource
    private LoanService loanService;
    
    @Resource
    private CustomerMapper customerMapper;
    
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRepayment(RepaymentDTO repaymentDTO, Long operatorId) {
        // 1. 验证贷款是否存在
        Loan loan = loanMapper.selectById(repaymentDTO.getLoanId());
        if (loan == null) {
            throw new ServiceException("贷款项目不存在");
        }
        
        // 2. 验证贷款状态（仅已放款/已逾期可还款）
        if (loan.getStatus() != 1 && loan.getStatus() != 3) {
            throw new ServiceException("只有已放款或已逾期的贷款可以还款");
        }
        
        // 3. 验证还款金额合法性
        BigDecimal repaymentAmount = repaymentDTO.getRepaymentAmount();
        if (repaymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("还款金额必须大于0");
        }
        if (repaymentAmount.compareTo(loan.getRemainingAmount()) > 0) {
            throw new ServiceException("还款金额不能超过剩余未还金额");
        }
        
        // 4. 验证本利和是否等于还款总金额
        if (repaymentDTO.getPrincipal() != null && repaymentDTO.getInterest() != null) {
            BigDecimal total = repaymentDTO.getPrincipal().add(repaymentDTO.getInterest());
            if (total.compareTo(repaymentAmount) != 0) {
                throw new ServiceException("本金与利息之和必须等于还款总金额");
            }
        }
        
        // 5. 创建还款记录
        RepaymentRecord record = new RepaymentRecord();
        BeanUtils.copyProperties(repaymentDTO, record);
        record.setCustomerId(loan.getCustomerId());
        record.setRepaymentTime(LocalDateTime.now());
        record.setOperatorId(operatorId);
        record.setStatus(1); // 默认还款成功
        record.setCreateTime(LocalDateTime.now());
        
        repaymentRecordMapper.insert(record);
        
        // 6. 调用贷款服务更新剩余金额
        loanService.updateRemainingAmount(loan.getId(), repaymentAmount);
    }

    @Override
    public RepaymentVO getById(Long id) {
        RepaymentRecord record = repaymentRecordMapper.selectById(id);
        return record != null ? convertToVO(record) : null;
    }

    @Override
    public List<RepaymentVO> listByLoanId(Long loanId) {
        List<RepaymentRecord> records = repaymentRecordMapper.selectByLoanId(loanId);
        return convertToVOList(records);
    }

    @Override
    public List<RepaymentVO> listByCustomerId(Long customerId) {
        List<RepaymentRecord> records = repaymentRecordMapper.selectByCustomerId(customerId);
        return convertToVOList(records);
    }

    @Override
    public BigDecimal getTotalAmountByLoanId(Long loanId) {
        return repaymentRecordMapper.selectTotalAmountByLoanId(loanId);
    }

    @Override
    public BigDecimal getTotalPrincipalByLoanId(Long loanId) {
        return repaymentRecordMapper.selectTotalPrincipalByLoanId(loanId);
    }

    /**
     * 转换为VO对象
     */
    private RepaymentVO convertToVO(RepaymentRecord record) {
        RepaymentVO vo = new RepaymentVO();
        BeanUtils.copyProperties(record, vo);
        
        // 设置状态名称
        vo.setStatusName(record.getStatus() == 1 ? "成功" : "失败");
        
        // 设置还款类型名称
        if (record.getRepaymentType() != null) {
            switch (record.getRepaymentType()) {
                case 1: vo.setRepaymentTypeName("正常还款"); break;
                case 2: vo.setRepaymentTypeName("提前还款"); break;
                case 3: vo.setRepaymentTypeName("逾期还款"); break;
                default: vo.setRepaymentTypeName("未知类型");
            }
        }
        
        // 查询企业名称
        if (record.getCustomerId() != null) {
            Customer customer = customerMapper.selectById(record.getCustomerId());
            if (customer != null) {
                vo.setCustomerName(customer.getEnterpriseName());
            }
        }
        
        // 查询贷款产品名称
        if (record.getLoanId() != null) {
            Loan loan = loanMapper.selectById(record.getLoanId());
            if (loan != null) {
                vo.setProductName(loan.getProductName());
            }
        }
        
        // 查询操作人姓名
        if (record.getOperatorId() != null) {
            SysUser operator = sysUserMapper.getById(record.getOperatorId());
            if (operator != null) {
                vo.setOperatorName(operator.getRealName());
            }
        }
        
        return vo;
    }

    /**
     * 转换为VO列表
     */
    private List<RepaymentVO> convertToVOList(List<RepaymentRecord> records) {
        List<RepaymentVO> voList = new ArrayList<>();
        for (RepaymentRecord record : records) {
            voList.add(convertToVO(record));
        }
        return voList;
    }
}
