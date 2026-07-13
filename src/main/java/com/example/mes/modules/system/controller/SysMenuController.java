package com.example.mes.modules.system.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.system.entity.SysMenu;
import com.example.mes.modules.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @PostMapping
    public Result<SysMenu> create(@RequestBody SysMenu menu) {
        return Result.ok(sysMenuService.createMenu(menu));
    }

    @PutMapping("/{id}")
    public Result<SysMenu> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        return Result.ok(sysMenuService.updateMenu(menu));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysMenuService.deleteMenu(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.ok(sysMenuService.getMenuById(id));
    }

    @GetMapping
    public Result<PageResult<SysMenu>> list(@RequestParam(defaultValue = "") String keyword,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(sysMenuService.listMenus(keyword, status, page, pageSize));
    }

    @GetMapping("/tree")
    public Result<List<TreeNode<SysMenu>>> tree() {
        return Result.ok(sysMenuService.getMenuTree());
    }
}
