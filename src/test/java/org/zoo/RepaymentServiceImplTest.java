package org.zoo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zoo.common.exception.ServiceException;
import org.zoo.loan.dal.Loan;
import org.zoo.loan.dal.LoanMapper;
import org.zoo.loan.facade.LoanService;
import org.zoo.repayment.dal.RepaymentRecord;
import org.zoo.repayment.dal.RepaymentRecordMapper;
import org.zoo.repayment.facade.impl.RepaymentServiceImpl;
import org.zoo.repayment.model.RepaymentDTO;
import org.zoo.repayment.model.RepaymentVO;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RepaymentServiceImplTest {

    @Mock
    private RepaymentRecordMapper repaymentRecordMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private RepaymentServiceImpl repaymentService;

    // 正常场景：创建还款记录成功
    @Test
    void createRepayment_Success() {
        // 准备数据
        RepaymentDTO dto = new RepaymentDTO();
        dto.setLoanId(1L);
        dto.setRepaymentAmount(new BigDecimal("5000"));
        dto.setPrincipal(new BigDecimal("4000"));
        dto.setInterest(new BigDecimal("1000"));
        dto.setRepaymentType(1);

        Long operatorId = 2L;

        // 模拟贷款存在且状态为已放款
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setCustomerId(100L);
        loan.setStatus(1); // 已放款
        loan.setRemainingAmount(new BigDecimal("10000"));
        when(loanMapper.selectById(1L)).thenReturn(loan);

        // 执行测试
        repaymentService.createRepayment(dto, operatorId);

        // 验证还款记录插入
        verify(repaymentRecordMapper).insert(argThat(record -> 
            record.getLoanId().equals(1L) &&
            record.getCustomerId().equals(100L) &&
            record.getRepaymentAmount().compareTo(new BigDecimal("5000")) == 0 &&
            record.getStatus() == 1 &&
            record.getOperatorId().equals(operatorId)
        ));

        // 验证贷款剩余金额更新
        verify(loanService).updateRemainingAmount(1L, new BigDecimal("5000"));
    }

    // 异常场景：贷款不存在
    @Test
    void createRepayment_LoanNotExists_ThrowsException() {
        RepaymentDTO dto = new RepaymentDTO();
        dto.setLoanId(999L); // 不存在的贷款ID
        dto.setRepaymentAmount(new BigDecimal("1000"));

        when(loanMapper.selectById(999L)).thenReturn(null);

        assertThatThrownBy(() -> repaymentService.createRepayment(dto, 2L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("贷款项目不存在");

        verify(repaymentRecordMapper, never()).insert(any());
        verify(loanService, never()).updateRemainingAmount(any(), any());
    }

    // 异常场景：贷款状态不允许还款（非已放款/已逾期）
    @Test
    void createRepayment_InvalidLoanStatus_ThrowsException() {
        RepaymentDTO dto = new RepaymentDTO();
        dto.setLoanId(1L);
        dto.setRepaymentAmount(new BigDecimal("1000"));

        // 模拟贷款状态为待审批（0）
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(0);
        when(loanMapper.selectById(1L)).thenReturn(loan);

        assertThatThrownBy(() -> repaymentService.createRepayment(dto, 2L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("只有已放款或已逾期的贷款可以还款");

        verify(repaymentRecordMapper, never()).insert(any());
    }

    // 异常场景：还款金额小于等于0
    @Test
    void createRepayment_AmountLessThanZero_ThrowsException() {
        RepaymentDTO dto = new RepaymentDTO();
        dto.setLoanId(1L);
        dto.setRepaymentAmount(new BigDecimal("-500")); // 非法金额

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(1); // 已放款
        when(loanMapper.selectById(1L)).thenReturn(loan);

        assertThatThrownBy(() -> repaymentService.createRepayment(dto, 2L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("还款金额必须大于0");
    }

    // 异常场景：还款金额超过剩余未还金额
    @Test
    void createRepayment_AmountExceedsRemaining_ThrowsException() {
        RepaymentDTO dto = new RepaymentDTO();
        dto.setLoanId(1L);
        dto.setRepaymentAmount(new BigDecimal("15000"));

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(1);
        loan.setRemainingAmount(new BigDecimal("10000")); // 剩余10000
        when(loanMapper.selectById(1L)).thenReturn(loan);

        assertThatThrownBy(() -> repaymentService.createRepayment(dto, 2L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("还款金额不能超过剩余未还金额");
    }

    // 异常场景：本金+利息不等于还款总金额
    @Test
    void createRepayment_PrincipalPlusInterestNotEqualTotal_ThrowsException() {
        RepaymentDTO dto = new RepaymentDTO();
        dto.setLoanId(1L);
        dto.setRepaymentAmount(new BigDecimal("5000"));
        dto.setPrincipal(new BigDecimal("3000"));
        dto.setInterest(new BigDecimal("1000")); // 3000+1000≠5000

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(1);
        loan.setRemainingAmount(new BigDecimal("10000"));
        when(loanMapper.selectById(1L)).thenReturn(loan);

        assertThatThrownBy(() -> repaymentService.createRepayment(dto, 2L))
                .isInstanceOf(ServiceException.class)
                .hasMessage("本金与利息之和必须等于还款总金额");
    }

    // 查询场景：根据ID查询还款记录
    @Test
    void getById_ReturnsRepaymentVO() {
        // 模拟数据库查询结果
        RepaymentRecord record = new RepaymentRecord();
        record.setId(1L);
        record.setLoanId(1L);
        record.setRepaymentAmount(new BigDecimal("5000"));
        record.setStatus(1);
        when(repaymentRecordMapper.selectById(1L)).thenReturn(record);

        // 执行查询
        RepaymentVO result = repaymentService.getById(1L);

        // 验证结果
        assert result != null;
        assert result.getId().equals(1L);
        assert result.getRepaymentAmount().compareTo(new BigDecimal("5000")) == 0;
        assert result.getStatusName().equals("成功");
    }

    // 查询场景：根据贷款ID查询总还款金额
    @Test
    void getTotalAmountByLoanId_ReturnsSum() {
        when(repaymentRecordMapper.selectTotalAmountByLoanId(1L))
                .thenReturn(new BigDecimal("20000"));

        BigDecimal total = repaymentService.getTotalAmountByLoanId(1L);
        assert total.compareTo(new BigDecimal("20000")) == 0;
    }
}