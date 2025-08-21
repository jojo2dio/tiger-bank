package org.zoo.loan.ws;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.zoo.common.result.Result;
import org.zoo.loan.facade.LoanGrantService;
import org.zoo.loan.model.LoanGrantDTO;
import org.zoo.loan.model.LoanGrantVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 放款记录控制器
 */
@RestController
@RequestMapping("/loan/grant")
@Api(tags = "放款记录管理", description = "贷款放款记录相关接口")
public class LoanGrantController {

    @Resource
    private LoanGrantService loanGrantService;

    @PostMapping
    @ApiOperation(value = "创建放款记录", notes = "为贷款项目创建放款记录")
    public Result<Void> createGrantRecord(@RequestBody LoanGrantDTO grantDTO) {
        // 实际项目中应从登录信息中获取当前用户ID
        Long operatorId = 1L;
        loanGrantService.createGrantRecord(grantDTO, operatorId);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询放款记录", notes = "通过放款记录ID获取详情")
    public Result<LoanGrantVO> getById(@PathVariable Long id) {
        return Result.success(loanGrantService.getById(id));
    }

    @GetMapping("/loan/{loanId}")
    @ApiOperation(value = "根据贷款ID查询放款记录", notes = "获取指定贷款项目的所有放款记录")
    public Result<List<LoanGrantVO>> listByLoanId(@PathVariable Long loanId) {
        return Result.success(loanGrantService.listByLoanId(loanId));
    }

    @GetMapping("/customer/{customerId}")
    @ApiOperation(value = "根据企业ID查询放款记录", notes = "获取指定企业的所有放款记录")
    public Result<List<LoanGrantVO>> listByCustomerId(@PathVariable Long customerId) {
        return Result.success(loanGrantService.listByCustomerId(customerId));
    }
}
