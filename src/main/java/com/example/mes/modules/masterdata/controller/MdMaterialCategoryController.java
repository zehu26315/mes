package com.example.mes.modules.masterdata.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.masterdata.entity.MdMaterialCategory;
import com.example.mes.modules.masterdata.service.MdMaterialCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/masterdata/material-categories")
@RequiredArgsConstructor
public class MdMaterialCategoryController {

    private final MdMaterialCategoryService mdMaterialCategoryService;

    @PostMapping
    public Result<MdMaterialCategory> create(@RequestBody MdMaterialCategory category) {
        return Result.ok(mdMaterialCategoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    public Result<MdMaterialCategory> update(@PathVariable Long id, @RequestBody MdMaterialCategory category) {
        category.setId(id);
        return Result.ok(mdMaterialCategoryService.updateCategory(category));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mdMaterialCategoryService.deleteCategory(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<MdMaterialCategory> getById(@PathVariable Long id) {
        return Result.ok(mdMaterialCategoryService.getCategoryById(id));
    }

    @GetMapping
    public Result<PageResult<MdMaterialCategory>> list(@RequestParam(defaultValue = "") String keyword,
                                                        @RequestParam(required = false) Integer status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(mdMaterialCategoryService.listCategories(keyword, status, page, pageSize));
    }
}
