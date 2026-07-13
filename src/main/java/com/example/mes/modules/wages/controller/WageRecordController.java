package com.example.mes.modules.wages.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.wages.entity.WageRecord;
import com.example.mes.modules.wages.service.WageRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wages")
@RequiredArgsConstructor
public class WageRecordController {

    private final WageRecordService wageRecordService;

    @GetMapping("/records")
    public Result<PageResult<WageRecord>> list(@RequestParam(defaultValue = "") String keyword,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(wageRecordService.listRecords(keyword, page, pageSize));
    }

    @PostMapping("/records")
    public Result<WageRecord> create(@RequestBody WageRecord record) {
        return Result.ok(wageRecordService.createRecord(record));
    }

    @GetMapping("/records/{id}")
    public Result<WageRecord> getById(@PathVariable Long id) {
        return Result.ok(wageRecordService.getRecordById(id));
    }

    @DeleteMapping("/records/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        wageRecordService.deleteRecord(id);
        return Result.ok();
    }
}
