package com.example.mes.modules.system.controller;

import com.example.mes.common.PageResult;
import com.example.mes.common.Result;
import com.example.mes.modules.system.entity.SysRole;
import com.example.mes.modules.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @PostMapping
    public Result<SysRole> create(@RequestBody SysRole role) {
        return Result.ok(sysRoleService.createRole(role));
    }

    @PutMapping("/{id}")
    public Result<SysRole> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        return Result.ok(sysRoleService.updateRole(role));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysRoleService.deleteRole(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.ok(sysRoleService.getRoleById(id));
    }

    @GetMapping("/all")
    public Result<List<SysRole>> listAll() {
        return Result.ok(sysRoleService.listAll());
    }

    @GetMapping
    public Result<PageResult<SysRole>> list(@RequestParam(defaultValue = "") String keyword,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(sysRoleService.listRoles(keyword, status, page, pageSize));
    }
}
