package com.example.mes.modules.quality.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.quality.entity.*;
import com.example.mes.modules.quality.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityInspectionCategoryService categoryService;
    private final QualityInspectionItemService itemService;
    private final QualityInspectionStandardService standardService;
    private final QualityStandardDetailService standardDetailService;
    private final QualityInspectionOrderService orderService;
    private final QualityInspectionResultService resultService;
    private final QualityFirstLastService firstLastService;
    private final QualityPatrolService patrolService;
    private final QualityDefectRecordService defectService;

    // ==================== Categories ====================

    @GetMapping("/categories")
    public Result<PageResult<QualityInspectionCategory>> listCategories(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(categoryService.listCategories(keyword, page, pageSize));
    }

    @PostMapping("/categories")
    public Result<QualityInspectionCategory> createCategory(@RequestBody QualityInspectionCategory category) {
        return Result.ok(categoryService.createCategory(category));
    }

    @GetMapping("/categories/{id}")
    public Result<QualityInspectionCategory> getCategory(@PathVariable Long id) {
        return Result.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/categories/{id}")
    public Result<QualityInspectionCategory> updateCategory(@PathVariable Long id, @RequestBody QualityInspectionCategory category) {
        category.setId(id);
        return Result.ok(categoryService.updateCategory(category));
    }

    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.ok();
    }

    // ==================== Items ====================

    @GetMapping("/items")
    public Result<PageResult<QualityInspectionItem>> listItems(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(itemService.listItems(keyword, categoryId, page, pageSize));
    }

    @PostMapping("/items")
    public Result<QualityInspectionItem> createItem(@RequestBody QualityInspectionItem item) {
        return Result.ok(itemService.createItem(item));
    }

    @GetMapping("/items/{id}")
    public Result<QualityInspectionItem> getItem(@PathVariable Long id) {
        return Result.ok(itemService.getItemById(id));
    }

    @PutMapping("/items/{id}")
    public Result<QualityInspectionItem> updateItem(@PathVariable Long id, @RequestBody QualityInspectionItem item) {
        item.setId(id);
        return Result.ok(itemService.updateItem(item));
    }

    @DeleteMapping("/items/{id}")
    public Result<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return Result.ok();
    }

    // ==================== Standards ====================

    @GetMapping("/standards")
    public Result<PageResult<QualityInspectionStandard>> listStandards(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(standardService.listStandards(keyword, status, page, pageSize));
    }

    @PostMapping("/standards")
    public Result<QualityInspectionStandard> createStandard(@RequestBody QualityInspectionStandard standard) {
        return Result.ok(standardService.createStandard(standard));
    }

    @GetMapping("/standards/{id}")
    public Result<QualityInspectionStandard> getStandard(@PathVariable Long id) {
        return Result.ok(standardService.getStandardById(id));
    }

    @PutMapping("/standards/{id}")
    public Result<QualityInspectionStandard> updateStandard(@PathVariable Long id, @RequestBody QualityInspectionStandard standard) {
        standard.setId(id);
        return Result.ok(standardService.updateStandard(standard));
    }

    @DeleteMapping("/standards/{id}")
    public Result<Void> deleteStandard(@PathVariable Long id) {
        standardService.deleteStandard(id);
        return Result.ok();
    }

    @GetMapping("/standards/{id}/details")
    public Result<List<QualityStandardDetail>> getStandardDetails(@PathVariable Long id) {
        return Result.ok(standardDetailService.getDetailsByStandardId(id));
    }

    @PostMapping("/standards/{id}/details")
    public Result<Void> saveStandardDetails(@PathVariable Long id, @RequestBody List<QualityStandardDetail> details) {
        standardDetailService.batchSaveDetails(id, details);
        return Result.ok();
    }

    // ==================== Orders ====================

    @GetMapping("/orders")
    public Result<PageResult<QualityInspectionOrder>> listOrders(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) String result,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(orderService.listOrders(keyword, orderType, result, page, pageSize));
    }

    @PostMapping("/orders")
    public Result<QualityInspectionOrder> createOrder(@RequestBody QualityInspectionOrder order) {
        return Result.ok(orderService.createOrder(order));
    }

    @GetMapping("/orders/{id}")
    public Result<QualityInspectionOrder> getOrder(@PathVariable Long id) {
        return Result.ok(orderService.getOrderById(id));
    }

    @PutMapping("/orders/{id}")
    public Result<QualityInspectionOrder> updateOrder(@PathVariable Long id, @RequestBody QualityInspectionOrder order) {
        order.setId(id);
        return Result.ok(orderService.updateOrder(order));
    }

    @DeleteMapping("/orders/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.ok();
    }

    @PutMapping("/orders/{id}/result")
    public Result<Void> updateOrderResult(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String result = (String) body.get("result");
        Integer defectQty = body.get("defectQty") != null ? ((Number) body.get("defectQty")).intValue() : null;
        orderService.updateOrderResult(id, result, defectQty);
        return Result.ok();
    }

    @GetMapping("/orders/{id}/results")
    public Result<List<QualityInspectionResult>> getOrderResults(@PathVariable Long id) {
        return Result.ok(resultService.getResultsByOrderId(id));
    }

    @PostMapping("/orders/{id}/results")
    public Result<Void> saveOrderResults(@PathVariable Long id, @RequestBody List<QualityInspectionResult> results) {
        resultService.batchSaveResults(id, results);
        return Result.ok();
    }

    // ==================== First/Last Inspection ====================

    @GetMapping("/first-last")
    public Result<PageResult<QualityFirstLast>> listFirstLast(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(required = false) String result,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(firstLastService.listRecords(keyword, workOrderId, result, page, pageSize));
    }

    @PostMapping("/first-last")
    public Result<QualityFirstLast> createFirstLast(@RequestBody QualityFirstLast record) {
        return Result.ok(firstLastService.createRecord(record));
    }

    @GetMapping("/first-last/{id}")
    public Result<QualityFirstLast> getFirstLast(@PathVariable Long id) {
        return Result.ok(firstLastService.getRecordById(id));
    }

    @PutMapping("/first-last/{id}")
    public Result<QualityFirstLast> updateFirstLast(@PathVariable Long id, @RequestBody QualityFirstLast record) {
        record.setId(id);
        return Result.ok(firstLastService.updateRecord(record));
    }

    @DeleteMapping("/first-last/{id}")
    public Result<Void> deleteFirstLast(@PathVariable Long id) {
        firstLastService.deleteRecord(id);
        return Result.ok();
    }

    // ==================== Patrols ====================

    @GetMapping("/patrols")
    public Result<PageResult<QualityPatrol>> listPatrols(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(required = false) String result,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(patrolService.listPatrols(keyword, workOrderId, result, page, pageSize));
    }

    @PostMapping("/patrols")
    public Result<QualityPatrol> createPatrol(@RequestBody QualityPatrol patrol) {
        return Result.ok(patrolService.createPatrol(patrol));
    }

    @GetMapping("/patrols/{id}")
    public Result<QualityPatrol> getPatrol(@PathVariable Long id) {
        return Result.ok(patrolService.getPatrolById(id));
    }

    @PutMapping("/patrols/{id}")
    public Result<QualityPatrol> updatePatrol(@PathVariable Long id, @RequestBody QualityPatrol patrol) {
        patrol.setId(id);
        return Result.ok(patrolService.updatePatrol(patrol));
    }

    @DeleteMapping("/patrols/{id}")
    public Result<Void> deletePatrol(@PathVariable Long id) {
        patrolService.deletePatrol(id);
        return Result.ok();
    }

    // ==================== Defects ====================

    @GetMapping("/defects")
    public Result<PageResult<QualityDefectRecord>> listDefects(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(required = false) String defectType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(defectService.listRecords(keyword, workOrderId, defectType, page, pageSize));
    }

    @PostMapping("/defects")
    public Result<QualityDefectRecord> createDefect(@RequestBody QualityDefectRecord record) {
        return Result.ok(defectService.createRecord(record));
    }

    @GetMapping("/defects/{id}")
    public Result<QualityDefectRecord> getDefect(@PathVariable Long id) {
        return Result.ok(defectService.getRecordById(id));
    }

    @PutMapping("/defects/{id}")
    public Result<QualityDefectRecord> updateDefect(@PathVariable Long id, @RequestBody QualityDefectRecord record) {
        record.setId(id);
        return Result.ok(defectService.updateRecord(record));
    }

    @DeleteMapping("/defects/{id}")
    public Result<Void> deleteDefect(@PathVariable Long id) {
        defectService.deleteRecord(id);
        return Result.ok();
    }
}
