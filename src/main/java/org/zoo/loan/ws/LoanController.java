package org.zoo.loan.ws;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.zoo.common.result.Result;
import org.zoo.common.util.CurrentHolder;
import org.zoo.loan.facade.LoanService;
import org.zoo.loan.model.LoanApprovalDTO;
import org.zoo.loan.model.LoanDTO;
import org.zoo.loan.model.LoanVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 贷款控制器（处理HTTP请求）
 */
@RestController
@RequestMapping("/loan")
@Api(tags = "贷款管理", description = "贷款信息管理接口")
public class LoanController {

    @Resource
    private LoanService loanService;

    @PostMapping
    @ApiOperation(value = "新增贷款", notes = "添加新的贷款项目信息")
    public Result<Void> add(@RequestBody LoanDTO loanDTO) {
        Long createUserId = CurrentHolder.getCurrentId();
        loanService.add(loanDTO, createUserId);
        return Result.success();
    }

    @PutMapping
    @ApiOperation(value = "更新贷款", notes = "修改已存在的贷款项目信息，仅待审批状态可修改")
    public Result<Void> update(@RequestBody LoanDTO loanDTO) {
        loanService.update(loanDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除贷款", notes = "根据ID删除贷款项目，仅待审批状态可删除")
    public Result<Void> deleteById(@PathVariable Long id) {
        loanService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询贷款", notes = "通过贷款项目ID获取详细信息")
    public Result<LoanVO> getById(@PathVariable Long id) {
        return Result.success(loanService.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    @ApiOperation(value = "根据企业ID查询贷款", notes = "获取指定企业的所有贷款项目")
    public Result<List<LoanVO>> listByCustomerId(@PathVariable Long customerId) {
        return Result.success(loanService.listByCustomerId(customerId));
    }

    @GetMapping
    @ApiOperation(value = "查询所有贷款", notes = "获取系统中所有贷款项目的列表")
    public Result<List<LoanVO>> listAll() {
        return Result.success(loanService.listAll());
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询贷款", notes = "分页获取贷款项目列表，支持起始页和每页条数参数")
    public Result<List<LoanVO>> page(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int limit) {
        return Result.success(loanService.page(start, limit));
    }

    @PostMapping("/approve")
    @ApiOperation(value = "审批贷款", notes = "对贷款项目进行审批操作，仅管理员可执行")
    public Result<Void> approve(@RequestBody LoanApprovalDTO approvalDTO) {
        Long approvalUserId = CurrentHolder.getCurrentId();
        loanService.approve(approvalDTO, approvalUserId);
        return Result.success();
    }
}
