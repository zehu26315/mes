package com.example.mes.modules.quality.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.quality.entity.QualityInspection;
import com.example.mes.modules.quality.service.QualityInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityInspectionService service;

    @GetMapping("/inspections")
    public Result<PageResult<QualityInspection>> list(@RequestParam(required = false) String keyword,
                                                       @RequestParam(required = false) String inspectionType,
                                                       @RequestParam(required = false) Integer result,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(service.list(keyword, inspectionType, result, page, pageSize));
    }

    @GetMapping("/inspections/{id}")
    public Result<QualityInspection> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    @PostMapping("/inspections")
    public Result<QualityInspection> create(@RequestBody QualityInspection entity) {
        return Result.ok(service.create(entity));
    }

    @PutMapping("/inspections/{id}")
    public Result<QualityInspection> update(@PathVariable Long id, @RequestBody QualityInspection entity) {
        entity.setId(id);
        return Result.ok(service.update(entity));
    }

    @DeleteMapping("/inspections/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        return Result.ok(service.stats());
    }
}
