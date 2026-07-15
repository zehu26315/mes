package com.example.mes.modules.barcode.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.barcode.entity.BarcodeRecord;
import com.example.mes.modules.barcode.entity.BarcodeRule;
import com.example.mes.modules.barcode.service.BarcodeRecordService;
import com.example.mes.modules.barcode.service.BarcodeRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/barcode")
@RequiredArgsConstructor
public class BarcodeController {

    private final BarcodeRuleService barcodeRuleService;
    private final BarcodeRecordService barcodeRecordService;

    // ==================== Barcode Rule ====================

    @PostMapping("/rules")
    public Result<BarcodeRule> createRule(@RequestBody BarcodeRule barcodeRule) {
        return Result.ok(barcodeRuleService.create(barcodeRule));
    }

    @PutMapping("/rules/{id}")
    public Result<BarcodeRule> updateRule(@PathVariable Long id, @RequestBody BarcodeRule barcodeRule) {
        barcodeRule.setId(id);
        return Result.ok(barcodeRuleService.update(barcodeRule));
    }

    @DeleteMapping("/rules/{id}")
    public Result<Void> deleteRule(@PathVariable Long id) {
        barcodeRuleService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/rules/{id}")
    public Result<BarcodeRule> getRuleById(@PathVariable Long id) {
        return Result.ok(barcodeRuleService.getById(id));
    }

    @GetMapping("/rules")
    public Result<PageResult<BarcodeRule>> listRules(@RequestParam(defaultValue = "") String keyword,
                                                      @RequestParam(required = false) Integer status,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(barcodeRuleService.list(keyword, status, page, pageSize));
    }

    // ==================== Barcode Record ====================

    @PostMapping("/generate")
    public Result<BarcodeRecord> generate(@RequestBody BarcodeRecord barcodeRecord) {
        return Result.ok(barcodeRecordService.create(barcodeRecord));
    }

    @GetMapping("/records/{id}")
    public Result<BarcodeRecord> getRecordById(@PathVariable Long id) {
        return Result.ok(barcodeRecordService.getById(id));
    }

    @GetMapping("/records")
    public Result<PageResult<BarcodeRecord>> listRecords(@RequestParam(defaultValue = "") String keyword,
                                                          @RequestParam(required = false) String status,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(barcodeRecordService.list(keyword, status, page, pageSize));
    }
}
