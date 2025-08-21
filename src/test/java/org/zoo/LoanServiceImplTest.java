package org.zoo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zoo.common.exception.ServiceException;
import org.zoo.common.util.CurrentHolder;
import org.zoo.customer.dal.Customer;
import org.zoo.customer.dal.CustomerMapper;
import org.zoo.loan.dal.Loan;
import org.zoo.loan.dal.LoanMapper;
import org.zoo.loan.facade.LoanGrantService;
import org.zoo.loan.facade.impl.LoanServiceImpl;
import org.zoo.loan.model.LoanApprovalDTO;
import org.zoo.loan.model.LoanDTO;
import org.zoo.loan.model.LoanGrantDTO;
import org.zoo.sysuser.dal.SysUserMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private LoanGrantService loanGrantService;

    @InjectMocks
    private LoanServiceImpl loanService;

    // 修复：新增贷款-客户不存在
    @Test
    void add_CustomerNotExists_ShouldThrowException() {
        // 准备数据
        LoanDTO dto = new LoanDTO();
        dto.setCustomerId(1L);
        dto.setAmount(new BigDecimal("10000"));

        // 模拟客户不存在
        when(customerMapper.selectById(1L)).thenReturn(new Customer());

        // 验证异常（修复断言逻辑）
        assertThatThrownBy(() -> loanService.add(dto, 1L))
                .isExactlyInstanceOf(ServiceException.class)
                .hasMessage("企业已存在");

        verify(loanMapper, never()).insert(any());
    }

    // 修复：新增贷款-金额非法
    @Test
    void add_InvalidAmount_ShouldThrowException() {
        LoanDTO dto = new LoanDTO();
        dto.setCustomerId(1L);
        dto.setAmount(new BigDecimal("-1000")); // 非法金额

        // 模拟客户存在
        when(customerMapper.selectById(1L)).thenReturn(null);

        assertThatThrownBy(() -> loanService.add(dto, 1L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("贷款金额必须大于0");
    }

    // 新增：更新贷款-贷款不存在
    @Test
    void update_LoanNotExists_ShouldThrowException() {
        LoanDTO dto = new LoanDTO();
        dto.setId(999L); // 不存在的ID

        when(loanMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> loanService.update(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessage("贷款项目不存在");
    }

    // 修复：更新贷款-非待审批状态
    @Test
    void update_NotPendingStatus_ShouldThrowException() {
        LoanDTO dto = new LoanDTO();
        dto.setId(1L);

        // 模拟贷款存在且状态为已放款（非待审批）
        Loan existingLoan = new Loan();
        existingLoan.setId(1L);
        existingLoan.setStatus(1); // 已放款
        when(loanMapper.selectById(1L)).thenReturn(existingLoan);

        assertThatThrownBy(() -> loanService.update(dto))
                .isInstanceOf(ServiceException.class)
                .hasMessage("只有待审批的贷款可以修改");
    }

    // 新增：删除贷款-贷款不存在
    @Test
    void deleteById_LoanNotExists_ShouldThrowException() {
        when(loanMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> loanService.deleteById(999L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("贷款项目不存在");
    }

    // 修复：审批贷款-非管理员操作
    @Test
    void approve_NotAdmin_ShouldThrowException() {
        // 使用MockedStatic修复CurrentHolder静态方法模拟
        try (MockedStatic<CurrentHolder> mockedStatic = mockStatic(CurrentHolder.class)) {
            // 模拟当前用户非管理员（ID=2）
            mockedStatic.when(CurrentHolder::getCurrentId).thenReturn(2L);

            LoanApprovalDTO dto = new LoanApprovalDTO();
            dto.setLoanId(1L);

            assertThatThrownBy(() -> loanService.approve(dto, 2L))
                    .isInstanceOf(ServiceException.class)
                    .hasMessage("只有管理员有资格审批");
        }
    }

    // 修复：审批贷款-贷款不存在
    @Test
    void approve_LoanNotExists_ShouldThrowException() {
        try (MockedStatic<CurrentHolder> mockedStatic = mockStatic(CurrentHolder.class)) {
            mockedStatic.when(CurrentHolder::getCurrentId).thenReturn(1L); // 管理员

            LoanApprovalDTO dto = new LoanApprovalDTO();
            dto.setLoanId(999L); // 不存在的贷款
            when(loanMapper.selectById(999L)).thenReturn(null);

            assertThatThrownBy(() -> loanService.approve(dto, 1L))
                    .isInstanceOf(ServiceException.class)
                    .hasMessage("贷款项目不存在");
        }
    }

    // 修复：审批贷款-非待审批状态
    @Test
    void approve_NotPendingStatus_ShouldThrowException() {
        try (MockedStatic<CurrentHolder> mockedStatic = mockStatic(CurrentHolder.class)) {
            mockedStatic.when(CurrentHolder::getCurrentId).thenReturn(1L);

            Loan loan = new Loan();
            loan.setId(1L);
            loan.setStatus(1); // 已放款（非待审批）
            when(loanMapper.selectById(1L)).thenReturn(loan);

            LoanApprovalDTO dto = new LoanApprovalDTO();
            dto.setLoanId(1L);

            assertThatThrownBy(() -> loanService.approve(dto, 1L))
                    .isInstanceOf(ServiceException.class)
                    .hasMessage("只有待审批的贷款可以进行审批操作");
        }
    }

    // 修复：审批贷款-状态非法
    @Test
    void approve_InvalidStatus_ShouldThrowException() {
        try (MockedStatic<CurrentHolder> mockedStatic = mockStatic(CurrentHolder.class)) {
            mockedStatic.when(CurrentHolder::getCurrentId).thenReturn(1L);

            Loan loan = new Loan();
            loan.setId(1L);
            loan.setStatus(0); // 待审批
            when(loanMapper.selectById(1L)).thenReturn(loan);

            LoanApprovalDTO dto = new LoanApprovalDTO();
            dto.setLoanId(1L);
            dto.setStatus(999); // 非法状态

            assertThatThrownBy(() -> loanService.approve(dto, 1L))
                    .isInstanceOf(ServiceException.class)
                    .hasMessage("审批状态只能是已放款(1)或审批拒绝(4)");
        }
    }

    // 修复：更新剩余金额-贷款不存在
    @Test
    void updateRemainingAmount_LoanNotExists_ShouldThrowException() {
        when(loanMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> loanService.updateRemainingAmount(999L, new BigDecimal("1000")))
                .isInstanceOf(ServiceException.class)
                .hasMessage("贷款项目不存在");
    }

    // 修复：更新剩余金额-状态非法
    @Test
    void updateRemainingAmount_InvalidStatus_ShouldThrowException() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(0); // 待审批（非法状态）
        when(loanMapper.selectById(1L)).thenReturn(loan);

        assertThatThrownBy(() -> loanService.updateRemainingAmount(1L, new BigDecimal("1000")))
                .isInstanceOf(ServiceException.class)
                .hasMessage("只有已放款或已逾期的贷款可以更新剩余金额");
    }

    // 修复：更新剩余金额-还款金额超限
    @Test
    void updateRemainingAmount_ExceedRemaining_ShouldThrowException() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(1); // 已放款
        loan.setRemainingAmount(new BigDecimal("5000")); // 剩余5000
        when(loanMapper.selectById(1L)).thenReturn(loan);

        // 尝试还款6000（超过剩余）
        assertThatThrownBy(() -> loanService.updateRemainingAmount(1L, new BigDecimal("6000")))
                .isInstanceOf(ServiceException.class)
                .hasMessage("还款金额不能大于剩余未还金额");
    }

    // 正常场景：审批通过
    @Test
    void approve_Approved_ShouldCreateGrantRecord() {
        try (MockedStatic<CurrentHolder> mockedStatic = mockStatic(CurrentHolder.class)) {
            mockedStatic.when(CurrentHolder::getCurrentId).thenReturn(1L); // 管理员

            // 模拟待审批贷款
            Loan loan = new Loan();
            loan.setId(1L);
            loan.setStatus(0);
            loan.setAmount(new BigDecimal("100000"));
            when(loanMapper.selectById(1L)).thenReturn(loan);

            // 构建审批DTO
            LoanApprovalDTO dto = new LoanApprovalDTO();
            dto.setLoanId(1L);
            dto.setStatus(1); // 审批通过
            dto.setApprovalRemark("同意放款");

            // 执行审批
            loanService.approve(dto, 1L);

            // 验证贷款状态更新
            verify(loanMapper).update(argThat(l ->
                    l.getStatus() == 1 &&
                            l.getGrantDate() != null &&
                            "同意放款".equals(l.getApprovalRemark())
            ));

            // 验证创建放款记录
            verify(loanGrantService).createGrantRecord(any(LoanGrantDTO.class), eq(1L));
        }
    }

    // 正常场景：更新剩余金额后结清
    @Test
    void updateRemainingAmount_FullRepayment_ShouldUpdateStatus() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(1); // 已放款
        loan.setRemainingAmount(new BigDecimal("5000"));
        when(loanMapper.selectById(1L)).thenReturn(loan);

        // 全额还款
        loanService.updateRemainingAmount(1L, new BigDecimal("5000"));

        // 验证剩余金额清零
        verify(loanMapper).updateRemainingAmount(1L, BigDecimal.ZERO);

        // 验证状态更新为已结清
        verify(loanMapper).update(argThat(l -> l.getStatus() == 2));
    }
}