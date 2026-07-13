package com.example.mes.modules.barcode.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.barcode.entity.BarcodeApplication;
import com.example.mes.modules.barcode.entity.BarcodeRecord;
import com.example.mes.modules.barcode.entity.BarcodeRule;
import com.example.mes.modules.barcode.entity.BarcodeTemplate;
import com.example.mes.modules.barcode.entity.BarcodeType;
import com.example.mes.modules.barcode.service.BarcodeApplicationService;
import com.example.mes.modules.barcode.service.BarcodeRecordService;
import com.example.mes.modules.barcode.service.BarcodeRuleService;
import com.example.mes.modules.barcode.service.BarcodeTemplateService;
import com.example.mes.modules.barcode.service.BarcodeTypeService;
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

    private final BarcodeTypeService barcodeTypeService;
    private final BarcodeRuleService barcodeRuleService;
    private final BarcodeTemplateService barcodeTemplateService;
    private final BarcodeApplicationService barcodeApplicationService;
    private final BarcodeRecordService barcodeRecordService;

    // ==================== Barcode Type ====================

    @PostMapping("/types")
    public Result<BarcodeType> createType(@RequestBody BarcodeType barcodeType) {
        return Result.ok(barcodeTypeService.create(barcodeType));
    }

    @PutMapping("/types/{id}")
    public Result<BarcodeType> updateType(@PathVariable Long id, @RequestBody BarcodeType barcodeType) {
        barcodeType.setId(id);
        return Result.ok(barcodeTypeService.update(barcodeType));
    }

    @DeleteMapping("/types/{id}")
    public Result<Void> deleteType(@PathVariable Long id) {
        barcodeTypeService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/types/{id}")
    public Result<BarcodeType> getTypeById(@PathVariable Long id) {
        return Result.ok(barcodeTypeService.getById(id));
    }

    @GetMapping("/types")
    public Result<PageResult<BarcodeType>> listTypes(@RequestParam(defaultValue = "") String keyword,
                                                      @RequestParam(required = false) Integer status,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(barcodeTypeService.list(keyword, status, page, pageSize));
    }

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

    // ==================== Barcode Template ====================

    @PostMapping("/templates")
    public Result<BarcodeTemplate> createTemplate(@RequestBody BarcodeTemplate barcodeTemplate) {
        return Result.ok(barcodeTemplateService.create(barcodeTemplate));
    }

    @PutMapping("/templates/{id}")
    public Result<BarcodeTemplate> updateTemplate(@PathVariable Long id, @RequestBody BarcodeTemplate barcodeTemplate) {
        barcodeTemplate.setId(id);
        return Result.ok(barcodeTemplateService.update(barcodeTemplate));
    }

    @DeleteMapping("/templates/{id}")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        barcodeTemplateService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/templates/{id}")
    public Result<BarcodeTemplate> getTemplateById(@PathVariable Long id) {
        return Result.ok(barcodeTemplateService.getById(id));
    }

    @GetMapping("/templates")
    public Result<PageResult<BarcodeTemplate>> listTemplates(@RequestParam(defaultValue = "") String keyword,
                                                              @RequestParam(required = false) Integer status,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(barcodeTemplateService.list(keyword, status, page, pageSize));
    }

    // ==================== Barcode Application ====================

    @PostMapping("/applications")
    public Result<BarcodeApplication> createApplication(@RequestBody BarcodeApplication barcodeApplication) {
        return Result.ok(barcodeApplicationService.create(barcodeApplication));
    }

    @PutMapping("/applications/{id}")
    public Result<BarcodeApplication> updateApplication(@PathVariable Long id, @RequestBody BarcodeApplication barcodeApplication) {
        barcodeApplication.setId(id);
        return Result.ok(barcodeApplicationService.update(barcodeApplication));
    }

    @DeleteMapping("/applications/{id}")
    public Result<Void> deleteApplication(@PathVariable Long id) {
        barcodeApplicationService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/applications/{id}")
    public Result<BarcodeApplication> getApplicationById(@PathVariable Long id) {
        return Result.ok(barcodeApplicationService.getById(id));
    }

    @GetMapping("/applications")
    public Result<PageResult<BarcodeApplication>> listApplications(@RequestParam(defaultValue = "") String keyword,
                                                                    @RequestParam(required = false) Integer status,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(barcodeApplicationService.list(keyword, status, page, pageSize));
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
                                                          @RequestParam(required = false) Integer status,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(barcodeRecordService.list(keyword, status, page, pageSize));
    }
}
