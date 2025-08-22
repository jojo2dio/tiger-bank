package org.zoo.repayment.ws;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.zoo.common.result.Result;
import org.zoo.common.util.CurrentHolder;
import org.zoo.repayment.facade.RepaymentService;
import org.zoo.repayment.model.RepaymentDTO;
import org.zoo.repayment.model.RepaymentVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 还款记录控制器
 */
@RestController
@RequestMapping("/repayment")
@Api(tags = "还款管理", description = "贷款还款记录相关接口")
public class RepaymentController {

    @Resource
    private RepaymentService repaymentService;

    @PostMapping
    @ApiOperation(value = "创建还款记录", notes = "新增还款记录并更新贷款剩余金额")
    public Result<Void> createRepayment(@RequestBody RepaymentDTO repaymentDTO) {
        Long operatorId = CurrentHolder.getCurrentId();
        repaymentService.createRepayment(repaymentDTO, operatorId);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询还款记录详情", notes = "根据ID查询还款记录详情")
    public Result<RepaymentVO> getById(@PathVariable Long id) {
        return Result.success(repaymentService.getById(id));
    }

    @GetMapping("/loan/{loanId}")
    @ApiOperation(value = "查询贷款的还款记录", notes = "根据贷款ID查询所有还款记录")
    public Result<List<RepaymentVO>> listByLoanId(@PathVariable Long loanId) {
        return Result.success(repaymentService.listByLoanId(loanId));
    }

    @GetMapping("/customer/{customerId}")
    @ApiOperation(value = "查询企业的还款记录", notes = "根据企业ID查询所有还款记录")
    public Result<List<RepaymentVO>> listByCustomerId(@PathVariable Long customerId) {
        return Result.success(repaymentService.listByCustomerId(customerId));
    }

    @GetMapping("/total/amount/{loanId}")
    @ApiOperation(value = "查询贷款总还款金额", notes = "根据贷款ID查询累计还款总金额")
    public Result<BigDecimal> getTotalAmountByLoanId(@PathVariable Long loanId) {
        return Result.success(repaymentService.getTotalAmountByLoanId(loanId));
    }

    @GetMapping("/total/principal/{loanId}")
    @ApiOperation(value = "查询贷款总还本金", notes = "根据贷款ID查询累计归还本金总额")
    public Result<BigDecimal> getTotalPrincipalByLoanId(@PathVariable Long loanId) {
        return Result.success(repaymentService.getTotalPrincipalByLoanId(loanId));
    }
}
