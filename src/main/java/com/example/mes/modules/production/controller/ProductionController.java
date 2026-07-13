package com.example.mes.modules.production.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.production.entity.ProdReporting;
import com.example.mes.modules.production.entity.ProdRepairOrder;
import com.example.mes.modules.production.entity.ProdWip;
import com.example.mes.modules.production.service.ProdReportingService;
import com.example.mes.modules.production.service.ProdRepairOrderService;
import com.example.mes.modules.production.service.ProdWipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/mgmt")
@RequiredArgsConstructor
public class ProductionController {

    private final ProdReportingService prodReportingService;
    private final ProdWipService prodWipService;
    private final ProdRepairOrderService prodRepairOrderService;

    // ---- Reportings ----

    @GetMapping("/reportings")
    public Result<PageResult<ProdReporting>> listReportings(@RequestParam(required = false) Long workOrderId,
                                                             @RequestParam(required = false) String processCode,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(prodReportingService.list(workOrderId, processCode, page, pageSize));
    }

    @PostMapping("/reportings")
    public Result<ProdReporting> createReporting(@RequestBody ProdReporting reporting) {
        return Result.ok(prodReportingService.create(reporting));
    }

    // ---- WIP ----

    @GetMapping("/wip")
    public Result<PageResult<ProdWip>> listWip(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(prodWipService.list(page, pageSize));
    }

    @GetMapping("/wip/process/{processCode}")
    public Result<List<ProdWip>> getWipByProcess(@PathVariable String processCode) {
        return Result.ok(prodWipService.getByProcessCode(processCode));
    }

    // ---- Repair Orders ----

    @GetMapping("/repair-orders")
    public Result<PageResult<ProdRepairOrder>> listRepairOrders(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(prodRepairOrderService.list(page, pageSize));
    }

    @PostMapping("/repair-orders")
    public Result<ProdRepairOrder> createRepairOrder(@RequestBody ProdRepairOrder repairOrder) {
        return Result.ok(prodRepairOrderService.create(repairOrder));
    }

    @PutMapping("/repair-orders/{id}/complete")
    public Result<Void> completeRepairOrder(@PathVariable Long id, @RequestParam String result) {
        prodRepairOrderService.complete(id, result);
        return Result.ok();
    }

    // ---- Completions ----

    @GetMapping("/completions")
    public Result<PageResult<ProdReporting>> listCompletions(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(prodReportingService.listCompletions(page, pageSize));
    }
}
