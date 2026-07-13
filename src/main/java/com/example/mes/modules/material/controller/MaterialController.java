package com.example.mes.modules.material.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.material.entity.MatInventory;
import com.example.mes.modules.material.entity.MatInventoryLog;
import com.example.mes.modules.material.entity.MatRequisition;
import com.example.mes.modules.material.entity.MatReturn;
import com.example.mes.modules.material.entity.MatSupplement;
import com.example.mes.modules.material.service.MatInventoryLogService;
import com.example.mes.modules.material.service.MatInventoryService;
import com.example.mes.modules.material.service.MatRequisitionService;
import com.example.mes.modules.material.service.MatReturnService;
import com.example.mes.modules.material.service.MatSupplementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MaterialController {

    private final MatRequisitionService matRequisitionService;
    private final MatReturnService matReturnService;
    private final MatSupplementService matSupplementService;
    private final MatInventoryService matInventoryService;
    private final MatInventoryLogService matInventoryLogService;

    // ==================== Requisitions ====================

    @PostMapping("/api/requisitions")
    public Result<MatRequisition> createRequisition(@RequestBody MatRequisition requisition) {
        return Result.ok(matRequisitionService.create(requisition));
    }

    @GetMapping("/api/requisitions")
    public Result<PageResult<MatRequisition>> listRequisitions(@RequestParam(required = false) String status,
                                                                @RequestParam(required = false) Long workOrderId,
                                                                @RequestParam(required = false) String materialCode,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(matRequisitionService.list(status, workOrderId, materialCode, page, pageSize));
    }

    @GetMapping("/api/requisitions/{id}")
    public Result<MatRequisition> getRequisitionById(@PathVariable Long id) {
        return Result.ok(matRequisitionService.getById(id));
    }

    @PutMapping("/api/requisitions/{id}/approve")
    public Result<Void> approveRequisition(@PathVariable Long id) {
        matRequisitionService.approve(id);
        return Result.ok();
    }

    // ==================== Returns ====================

    @PostMapping("/api/returns")
    public Result<MatReturn> createReturn(@RequestBody MatReturn matReturn) {
        return Result.ok(matReturnService.create(matReturn));
    }

    @GetMapping("/api/returns")
    public Result<PageResult<MatReturn>> listReturns(@RequestParam(required = false) String status,
                                                      @RequestParam(required = false) Long workOrderId,
                                                      @RequestParam(required = false) String materialCode,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(matReturnService.list(status, workOrderId, materialCode, page, pageSize));
    }

    @GetMapping("/api/returns/{id}")
    public Result<MatReturn> getReturnById(@PathVariable Long id) {
        return Result.ok(matReturnService.getById(id));
    }

    // ==================== Supplements ====================

    @PostMapping("/api/supplements")
    public Result<MatSupplement> createSupplement(@RequestBody MatSupplement supplement) {
        return Result.ok(matSupplementService.create(supplement));
    }

    @GetMapping("/api/supplements")
    public Result<PageResult<MatSupplement>> listSupplements(@RequestParam(required = false) String status,
                                                              @RequestParam(required = false) Long workOrderId,
                                                              @RequestParam(required = false) String materialCode,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(matSupplementService.list(status, workOrderId, materialCode, page, pageSize));
    }

    @GetMapping("/api/supplements/{id}")
    public Result<MatSupplement> getSupplementById(@PathVariable Long id) {
        return Result.ok(matSupplementService.getById(id));
    }

    @PutMapping("/api/supplements/{id}/approve")
    public Result<Void> approveSupplement(@PathVariable Long id) {
        matSupplementService.approve(id);
        return Result.ok();
    }

    // ==================== Inventory ====================

    @GetMapping("/api/inventory")
    public Result<PageResult<MatInventory>> listInventory(@RequestParam(required = false) Long materialId,
                                                           @RequestParam(required = false) String materialCode,
                                                           @RequestParam(required = false) String warehouse,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(matInventoryService.list(materialId, materialCode, warehouse, page, pageSize));
    }

    @GetMapping("/api/inventory/low-stock")
    public Result<PageResult<MatInventory>> getLowStock(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(matInventoryService.getLowStock(page, pageSize));
    }

    // ==================== Inventory Logs ====================

    @GetMapping("/api/inventory-logs")
    public Result<PageResult<MatInventoryLog>> listInventoryLogs(@RequestParam(required = false) Long materialId,
                                                                  @RequestParam(required = false) String materialCode,
                                                                  @RequestParam(required = false) String changeType,
                                                                  @RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(matInventoryLogService.list(materialId, materialCode, changeType, page, pageSize));
    }
}
