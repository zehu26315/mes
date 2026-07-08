package com.example.mes.modules.system.controller;

import com.example.mes.common.PageResult;
import com.example.mes.common.Result;
import com.example.mes.modules.system.entity.SysUser;
import com.example.mes.modules.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @PostMapping
    public Result<SysUser> create(@RequestBody SysUser user) {
        return Result.ok(sysUserService.createUser(user));
    }

    @PutMapping("/{id}")
    public Result<SysUser> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        return Result.ok(sysUserService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.ok(sysUserService.getUserById(id));
    }

    @GetMapping
    public Result<PageResult<SysUser>> list(@RequestParam(defaultValue = "") String keyword,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(sysUserService.listUsers(keyword, status, page, pageSize));
    }

    @PostMapping("/{id}/roles")
    public Result<Void> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        sysUserService.assignRoles(id, roleIds);
        return Result.ok();
    }
}
