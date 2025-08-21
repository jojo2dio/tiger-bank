package org.zoo.loan.facade.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zoo.common.exception.ServiceException;
import org.zoo.common.util.CurrentHolder;
import org.zoo.customer.dal.Customer;
import org.zoo.customer.dal.CustomerMapper;
import org.zoo.loan.dal.Loan;
import org.zoo.loan.dal.LoanMapper;
import org.zoo.loan.facade.LoanGrantService;
import org.zoo.loan.facade.LoanService;
import org.zoo.loan.model.LoanApprovalDTO;
import org.zoo.loan.model.LoanDTO;
import org.zoo.loan.model.LoanGrantDTO;
import org.zoo.loan.model.LoanVO;
import org.zoo.sysuser.dal.SysUser;
import org.zoo.sysuser.dal.SysUserMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 贷款服务实现类
 */
@Service
public class LoanServiceImpl implements LoanService {

    @Resource
    private LoanMapper loanMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private LoanGrantService loanGrantService;

    @Override
    public void add(LoanDTO loanDTO, Long createUserId) {
        // 验证客户是否存在
        Customer customer = customerMapper.selectById(loanDTO.getCustomerId());
        if (customer == null) {
            throw new ServiceException("企业不存在");
        }

        // 验证贷款金额是否合法
        if (loanDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("贷款金额必须大于0");
        }

        // DTO转换为Entity
        Loan loan = new Loan();
        BeanUtils.copyProperties(loanDTO, loan);

        // 新增时剩余金额等于总金额
        loan.setRemainingAmount(loanDTO.getAmount());

        // 设置创建信息
        loan.setCreateUserId(createUserId);
        loan.setCreateTime(LocalDateTime.now());
        loan.setUpdateTime(LocalDateTime.now());

        // 初始状态为待审批
        if (loan.getStatus() == null) {
            loan.setStatus(0);
        }

        loanMapper.insert(loan);
    }

    @Override
    public void update(LoanDTO loanDTO) {
        // 检查贷款是否存在
        Loan existingLoan = loanMapper.selectById(loanDTO.getId());
        if (existingLoan == null) {
            throw new ServiceException("贷款项目不存在");
        }

        // 已审批的贷款不允许修改
        if (existingLoan.getStatus() != 0) {
            throw new ServiceException("只有待审批的贷款可以修改");
        }

        // 验证客户是否存在
        if (loanDTO.getCustomerId() != null) {
            Customer customer = customerMapper.selectById(loanDTO.getCustomerId());
            if (customer == null) {
                throw new ServiceException("企业不存在");
            }
        }

        // DTO转换为Entity
        Loan loan = new Loan();
        BeanUtils.copyProperties(loanDTO, loan);
        loan.setUpdateTime(LocalDateTime.now());

        // 如果修改了总金额，同步更新剩余金额
        if (loanDTO.getAmount() != null) {
            loan.setRemainingAmount(loanDTO.getAmount());
        }

        loanMapper.update(loan);
    }

    @Override
    public void deleteById(Long id) {
        // 检查贷款是否存在
        Loan loan = loanMapper.selectById(id);
        if (loan == null) {
            throw new ServiceException("贷款项目不存在");
        }

        // 只有待审批的贷款可以删除
        if (loan.getStatus() != 0) {
            throw new ServiceException("只有待审批的贷款可以删除");
        }

        loanMapper.deleteById(id);
    }

    @Override
    public LoanVO getById(Long id) {
        Loan loan = loanMapper.selectById(id);
        if (loan == null) {
            return null;
        }
        return convertToVO(loan);
    }

    @Override
    public List<LoanVO> listByCustomerId(Long customerId) {
        List<Loan> loans = loanMapper.selectByCustomerId(customerId);
        return convertToVOList(loans);
    }

    @Override
    public List<LoanVO> listAll() {
        List<Loan> loans = loanMapper.selectAll();
        return convertToVOList(loans);
    }

    @Override
    public List<LoanVO> page(int start, int limit) {
        List<Loan> loans = loanMapper.selectByPage(start, limit);
        return convertToVOList(loans);
    }

    @Override
    public void approve(LoanApprovalDTO approvalDTO, Long approvalUserId) {
        Long createUserId = CurrentHolder.getCurrentId();
        if (createUserId != 1) {
            throw new ServiceException("只有管理员有资格审批");
        }
        // 检查贷款是否存在
        Loan loan = loanMapper.selectById(approvalDTO.getLoanId());
        if (loan == null) {
            throw new ServiceException("贷款项目不存在");
        }

        // 检查是否为待审批状态
        if (loan.getStatus() != 0) {
            throw new ServiceException("只有待审批的贷款可以进行审批操作");
        }

        // 检查审批状态是否合法
        if (approvalDTO.getStatus() != 1 && approvalDTO.getStatus() != 4) {
            throw new ServiceException("审批状态只能是已放款(1)或审批拒绝(4)");
        }

        // 更新贷款信息
        loan.setStatus(approvalDTO.getStatus());
        loan.setApprovalUserId(approvalUserId);
        loan.setApprovalTime(LocalDateTime.now());
        loan.setApprovalRemark(approvalDTO.getApprovalRemark());
        loan.setUpdateTime(LocalDateTime.now());

        // 如果是审批通过，设置放款日期并创建放款记录
        if (approvalDTO.getStatus() == 1) {
            loan.setGrantDate(LocalDate.now());
            loanMapper.update(loan);

            // 自动创建放款记录（放款金额等于贷款总金额）
            LoanGrantDTO grantDTO = new LoanGrantDTO();
            grantDTO.setLoanId(loan.getId());
            grantDTO.setGrantAmount(loan.getAmount());
            grantDTO.setRemark("系统自动创建的放款记录：" + approvalDTO.getApprovalRemark());

            loanGrantService.createGrantRecord(grantDTO, approvalUserId);
        } else {
            loanMapper.update(loan);
        }
    }

    @Override
    public void updateRemainingAmount(Long loanId, BigDecimal amount) {
        // 检查贷款是否存在
        Loan loan = loanMapper.selectById(loanId);
        if (loan == null) {
            throw new ServiceException("贷款项目不存在");
        }

        // 检查贷款状态是否为已放款或已逾期
        if (loan.getStatus() != 1 && loan.getStatus() != 3) {
            throw new ServiceException("只有已放款或已逾期的贷款可以更新剩余金额");
        }

        // 计算新的剩余金额
        BigDecimal newRemainingAmount = loan.getRemainingAmount().subtract(amount);

        // 确保剩余金额不小于0
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("还款金额不能大于剩余未还金额");
        }

        // 更新剩余金额
        loanMapper.updateRemainingAmount(loanId, newRemainingAmount);

        // 如果剩余金额为0，更新状态为已结清
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(2);
            loan.setUpdateTime(LocalDateTime.now());
            loanMapper.update(loan);
        }
    }

    /**
     * 将Entity转换为VO
     */
    private LoanVO convertToVO(Loan loan) {
        LoanVO vo = new LoanVO();
        BeanUtils.copyProperties(loan, vo);

        // 设置状态中文名称
        if (loan.getStatus() != null) {
            switch (loan.getStatus()) {
                case 0: vo.setStatusName("待审批"); break;
                case 1: vo.setStatusName("已放款"); break;
                case 2: vo.setStatusName("已结清"); break;
                case 3: vo.setStatusName("已逾期"); break;
                case 4: vo.setStatusName("审批拒绝"); break;
                default: vo.setStatusName("未知状态");
            }
        }

        // 查询企业名称
        if (loan.getCustomerId() != null) {
            Customer customer = customerMapper.selectById(loan.getCustomerId());
            if (customer != null) {
                vo.setCustomerName(customer.getEnterpriseName());
            }
        }

        // 查询创建人姓名
        if (loan.getCreateUserId() != null) {
            SysUser createUser = sysUserMapper.getById(loan.getCreateUserId());
            if (createUser != null) {
                vo.setCreateUserName(createUser.getRealName());
            }
        }

        // 查询审批人姓名
        if (loan.getApprovalUserId() != null) {
            SysUser approvalUser = sysUserMapper.getById(loan.getApprovalUserId());
            if (approvalUser != null) {
                vo.setApprovalUserName(approvalUser.getRealName());
            }
        }

        return vo;
    }

    /**
     * 将Entity列表转换为VO列表
     */
    private List<LoanVO> convertToVOList(List<Loan> loans) {
        List<LoanVO> voList = new ArrayList<>();
        for (Loan loan : loans) {
            voList.add(convertToVO(loan));
        }
        return voList;
    }
}
