package com.example.mes.modules.wages.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.wages.entity.PieceWage;
import com.example.mes.modules.wages.service.PieceWageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wages")
@RequiredArgsConstructor
public class WagesController {

    private final PieceWageService service;

    @GetMapping
    public Result<PageResult<PieceWage>> list(@RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(required = false) String wageDate,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(service.list(keyword, status, wageDate, page, pageSize));
    }

    @GetMapping("/{id}")
    public Result<PieceWage> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    @PostMapping
    public Result<PieceWage> create(@RequestBody PieceWage entity) {
        return Result.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public Result<PieceWage> update(@PathVariable Long id, @RequestBody PieceWage entity) {
        entity.setId(id);
        return Result.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }
}
