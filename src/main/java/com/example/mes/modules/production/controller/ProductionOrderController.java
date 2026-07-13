package com.example.mes.modules.production.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.production.entity.ProdDispatchOrder;
import com.example.mes.modules.production.entity.ProdWorkOrder;
import com.example.mes.modules.production.service.ProdDispatchOrderService;
import com.example.mes.modules.production.service.ProdWorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProdWorkOrderService prodWorkOrderService;
    private final ProdDispatchOrderService prodDispatchOrderService;

    // ---- Work Orders ----

    @GetMapping("/work-orders")
    public Result<PageResult<ProdWorkOrder>> listWorkOrders(@RequestParam(defaultValue = "") String keyword,
                                                             @RequestParam(required = false) Integer status,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(prodWorkOrderService.list(keyword, status, page, pageSize));
    }

    @PostMapping("/work-orders")
    public Result<ProdWorkOrder> createWorkOrder(@RequestBody ProdWorkOrder workOrder) {
        return Result.ok(prodWorkOrderService.create(workOrder));
    }

    @PutMapping("/work-orders/{id}")
    public Result<ProdWorkOrder> updateWorkOrder(@PathVariable Long id, @RequestBody ProdWorkOrder workOrder) {
        workOrder.setId(id);
        return Result.ok(prodWorkOrderService.update(workOrder));
    }

    @DeleteMapping("/work-orders/{id}")
    public Result<Void> deleteWorkOrder(@PathVariable Long id) {
        prodWorkOrderService.delete(id);
        return Result.ok();
    }

    @GetMapping("/work-orders/{id}")
    public Result<ProdWorkOrder> getWorkOrder(@PathVariable Long id) {
        return Result.ok(prodWorkOrderService.getById(id));
    }

    @PutMapping("/work-orders/{id}/status")
    public Result<Void> updateWorkOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        prodWorkOrderService.updateStatus(id, status);
        return Result.ok();
    }

    // ---- Dispatch Orders ----

    @GetMapping("/dispatch-orders")
    public Result<PageResult<ProdDispatchOrder>> listDispatchOrders(@RequestParam(required = false) Long workOrderId,
                                                                     @RequestParam(required = false) Integer status,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(prodDispatchOrderService.list(workOrderId, status, page, pageSize));
    }

    @PostMapping("/dispatch-orders")
    public Result<ProdDispatchOrder> createDispatchOrder(@RequestBody ProdDispatchOrder dispatchOrder) {
        return Result.ok(prodDispatchOrderService.create(dispatchOrder));
    }

    @PutMapping("/dispatch-orders/{id}")
    public Result<ProdDispatchOrder> updateDispatchOrder(@PathVariable Long id, @RequestBody ProdDispatchOrder dispatchOrder) {
        dispatchOrder.setId(id);
        return Result.ok(prodDispatchOrderService.update(dispatchOrder));
    }
}
