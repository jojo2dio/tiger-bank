package org.zoo.customer.ws;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.zoo.common.result.Result;
import org.zoo.common.util.CurrentHolder;
import org.zoo.customer.facade.CustomerService;
import org.zoo.customer.model.CustomerDTO;
import org.zoo.customer.model.CustomerVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户控制器（处理HTTP请求）
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "客户管理", description = "贷款客户信息管理接口")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping
    @ApiOperation(value = "新增客户", notes = "添加新的贷款客户信息")
    public Result<Void> add(@RequestBody CustomerDTO customerDTO) {
        Long createUserId = CurrentHolder.getCurrentId();
        customerService.add(customerDTO, createUserId);
        return Result.success();
    }

    @PutMapping
    @ApiOperation(value = "更新客户", notes = "修改已存在的客户信息")
    public Result<Void> update(@RequestBody CustomerDTO customerDTO) {
        customerService.update(customerDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除客户", notes = "根据ID删除客户信息")
    public Result<Void> deleteById(@PathVariable Long id) {
        customerService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询客户", notes = "通过客户ID获取详细信息")
    public Result<CustomerVO> getById(@PathVariable Long id) {
        return Result.success(customerService.getById(id));
    }

    @GetMapping("/credit/{creditCode}")
    @ApiOperation(value = "根据统一社会信用代码查询客户", notes = "通过信用代码获取客户信息")
    public Result<CustomerVO> getByCreditCode(@PathVariable String creditCode) {
        return Result.success(customerService.getByCreditCode(creditCode));
    }

    @GetMapping
    @ApiOperation(value = "查询所有客户", notes = "获取系统中所有客户的列表")
    public Result<List<CustomerVO>> listAll() {
        return Result.success(customerService.listAll());
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询客户", notes = "分页获取客户列表，支持起始页和每页条数参数")
    public Result<List<CustomerVO>> page(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int limit) {
        return Result.success(customerService.page(start, limit));
    }
    @GetMapping("/queryParams")
    @ApiOperation(value = "条件查询客户", notes = "根据信用代码和企业名称查询客户，参数可任选")
    public Result<List<CustomerVO>> queryCustomers(
            @RequestParam(required = false) String creditCode,
            @RequestParam(required = false) String enterpriseName) {
        List<CustomerVO> customerVOs = customerService.queryByConditions(creditCode, enterpriseName);
        return Result.success(customerVOs);
    }
}
