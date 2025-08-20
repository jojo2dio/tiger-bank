package org.zoo.sysuser.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zoo.common.result.Result;
import org.zoo.sysuser.entity.SysUser;
import org.zoo.sysuser.service.SysUserService;

import java.util.Map;

@RestController
@RequestMapping("/sys/user")
@Api(tags = "系统用户管理")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public Result add(@RequestBody SysUser sysUser) {
        sysUserService.add(sysUser);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("根据ID删除用户")
    public Result deleteById(@PathVariable Long id) {
        sysUserService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public Result update(@RequestBody SysUser sysUser) {
        sysUserService.update(sysUser);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID获取用户")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    @GetMapping("/getByUsername/{username}")
    @ApiOperation("根据用户名获取用户")
    public Result<SysUser> getByUsername(@PathVariable String username) {
        return Result.success(sysUserService.getByUsername(username));
    }

    @GetMapping("/list")
    @ApiOperation("获取所有用户列表")
    public Result list() {
        return Result.success(sysUserService.list());
    }

    @GetMapping("/page")
    @ApiOperation("分页获取用户列表")
    public Result<Map<String, Object>> page(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int limit) {
        return Result.success(sysUserService.page(start, limit));
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<SysUser> login(
            @RequestParam String username,
            @RequestParam String password) {
        return Result.success(sysUserService.login(username, password));
    }
}
