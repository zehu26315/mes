package com.example.mes.modules.masterdata.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.masterdata.entity.MdMaterial;
import com.example.mes.modules.masterdata.service.MdMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/masterdata/materials")
@RequiredArgsConstructor
public class MdMaterialController {

    private final MdMaterialService mdMaterialService;

    @PostMapping
    public Result<MdMaterial> create(@RequestBody MdMaterial material) {
        return Result.ok(mdMaterialService.createMaterial(material));
    }

    @PutMapping("/{id}")
    public Result<MdMaterial> update(@PathVariable Long id, @RequestBody MdMaterial material) {
        material.setId(id);
        return Result.ok(mdMaterialService.updateMaterial(material));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mdMaterialService.deleteMaterial(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<MdMaterial> getById(@PathVariable Long id) {
        return Result.ok(mdMaterialService.getMaterialById(id));
    }

    @GetMapping
    public Result<PageResult<MdMaterial>> list(@RequestParam(defaultValue = "") String keyword,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(mdMaterialService.listMaterials(keyword, status, page, pageSize));
    }
}
