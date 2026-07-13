package com.example.mes.modules.system.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.system.entity.SysDepartment;
import com.example.mes.modules.system.service.SysDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/departments")
@RequiredArgsConstructor
public class SysDepartmentController {

    private final SysDepartmentService sysDepartmentService;

    @PostMapping
    public Result<SysDepartment> create(@RequestBody SysDepartment dept) {
        return Result.ok(sysDepartmentService.createDepartment(dept));
    }

    @PutMapping("/{id}")
    public Result<SysDepartment> update(@PathVariable Long id, @RequestBody SysDepartment dept) {
        dept.setId(id);
        return Result.ok(sysDepartmentService.updateDepartment(dept));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysDepartmentService.deleteDepartment(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<SysDepartment> getById(@PathVariable Long id) {
        return Result.ok(sysDepartmentService.getDepartmentById(id));
    }

    @GetMapping
    public Result<PageResult<SysDepartment>> list(@RequestParam(defaultValue = "") String keyword,
                                                   @RequestParam(required = false) Integer status,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(sysDepartmentService.listDepartments(keyword, status, page, pageSize));
    }

    @GetMapping("/tree")
    public Result<List<TreeNode<SysDepartment>>> tree() {
        return Result.ok(sysDepartmentService.getDepartmentTree());
    }
}
