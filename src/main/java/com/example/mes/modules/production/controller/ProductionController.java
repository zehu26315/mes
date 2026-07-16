package com.example.mes.modules.production.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.production.entity.*;
import com.example.mes.modules.production.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionOrderService orderService;
    private final ProWorkOrderService workOrderService;
    private final ProTaskService taskService;
    private final ProDispatchService dispatchService;
    private final ProCompletionService completionService;
    private final ProRepairService repairService;
    private final ProReportService reportService;

    // ==================== ProductionOrder ====================

    @GetMapping("/orders")
    public Result<PageResult<ProductionOrder>> listOrders(@RequestParam(required = false) String keyword,
                                                           @RequestParam(required = false) Integer status,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(orderService.list(keyword, status, page, pageSize));
    }

    @GetMapping("/orders/{id}")
    public Result<ProductionOrder> getOrder(@PathVariable Long id) {
        return Result.ok(orderService.getById(id));
    }

    @PostMapping("/orders")
    public Result<ProductionOrder> createOrder(@RequestBody ProductionOrder entity) {
        return Result.ok(orderService.create(entity));
    }

    @PutMapping("/orders/{id}")
    public Result<ProductionOrder> updateOrder(@PathVariable Long id, @RequestBody ProductionOrder entity) {
        entity.setId(id);
        return Result.ok(orderService.update(entity));
    }

    @DeleteMapping("/orders/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return Result.ok();
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return Result.ok(orderService.dashboardStats());
    }

    // ==================== ProWorkOrder ====================

    @GetMapping("/work-orders")
    public Result<PageResult<ProWorkOrder>> listWorkOrders(@RequestParam(required = false) String workOrderNo,
                                                            @RequestParam(required = false) String keyword,
                                                            @RequestParam(required = false) Long materialId,
                                                            @RequestParam(required = false) Integer status,
                                                            @RequestParam(required = false) Integer sourceType,
                                                            @RequestParam(required = false) Integer priority,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int pageSize) {
        String searchTerm = keyword != null ? keyword : workOrderNo;
        return Result.ok(workOrderService.list(searchTerm, materialId, status, sourceType, priority, page, pageSize));
    }

    @GetMapping("/work-orders/{id}")
    public Result<ProWorkOrder> getWorkOrder(@PathVariable Long id) {
        return Result.ok(workOrderService.getById(id));
    }

    @PostMapping("/work-orders")
    public Result<ProWorkOrder> createWorkOrder(@RequestBody ProWorkOrder entity) {
        return Result.ok(workOrderService.create(entity));
    }

    @PutMapping("/work-orders/{id}")
    public Result<ProWorkOrder> updateWorkOrder(@PathVariable Long id, @RequestBody ProWorkOrder entity) {
        entity.setId(id);
        return Result.ok(workOrderService.update(entity));
    }

    @DeleteMapping("/work-orders/{id}")
    public Result<Void> deleteWorkOrder(@PathVariable Long id) {
        workOrderService.delete(id);
        return Result.ok();
    }

    // ==================== ProTask ====================

    @GetMapping("/tasks")
    public Result<PageResult<ProTask>> listTasks(@RequestParam(required = false) String keyword,
                                                  @RequestParam(required = false) String taskNo,
                                                  @RequestParam(required = false) Long workOrderId,
                                                  @RequestParam(required = false) String productionLine,
                                                  @RequestParam(required = false) Integer status,
                                                  @RequestParam(required = false) Integer sourceType,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(taskService.list(keyword != null ? keyword : taskNo, workOrderId, productionLine, status, sourceType, page, pageSize));
    }

    @GetMapping("/tasks/{id}")
    public Result<ProTask> getTask(@PathVariable Long id) {
        return Result.ok(taskService.getById(id));
    }

    @PostMapping("/tasks")
    public Result<ProTask> createTask(@RequestBody ProTask entity) {
        return Result.ok(taskService.create(entity));
    }

    @PutMapping("/tasks/{id}")
    public Result<ProTask> updateTask(@PathVariable Long id, @RequestBody ProTask entity) {
        entity.setId(id);
        return Result.ok(taskService.update(entity));
    }

    @DeleteMapping("/tasks/{id}")
    public Result<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return Result.ok();
    }

    // ==================== ProDispatch ====================

    @GetMapping("/dispatches")
    public Result<PageResult<ProDispatch>> listDispatches(@RequestParam(required = false) String keyword,
                                                           @RequestParam(required = false) String dispatchNo,
                                                           @RequestParam(required = false) Long workOrderId,
                                                           @RequestParam(required = false) String productionLine,
                                                           @RequestParam(required = false) Integer status,
                                                           @RequestParam(required = false) Long assigneeId,
                                                           @RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(dispatchService.list(keyword != null ? keyword : dispatchNo, workOrderId, productionLine, status, assigneeId, page, pageSize));
    }

    @GetMapping("/dispatches/{id}")
    public Result<ProDispatch> getDispatch(@PathVariable Long id) {
        return Result.ok(dispatchService.getById(id));
    }

    @PostMapping("/dispatches")
    public Result<ProDispatch> createDispatch(@RequestBody ProDispatch entity) {
        return Result.ok(dispatchService.create(entity));
    }

    @PutMapping("/dispatches/{id}")
    public Result<ProDispatch> updateDispatch(@PathVariable Long id, @RequestBody ProDispatch entity) {
        entity.setId(id);
        return Result.ok(dispatchService.update(entity));
    }

    @DeleteMapping("/dispatches/{id}")
    public Result<Void> deleteDispatch(@PathVariable Long id) {
        dispatchService.delete(id);
        return Result.ok();
    }

    // ==================== ProCompletion ====================

    @GetMapping("/completions")
    public Result<PageResult<ProCompletion>> listCompletions(@RequestParam(required = false) String keyword,
                                                              @RequestParam(required = false) String completionNo,
                                                              @RequestParam(required = false) Long workOrderId,
                                                              @RequestParam(required = false) Long materialId,
                                                              @RequestParam(required = false) Integer status,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(completionService.list(keyword != null ? keyword : completionNo, workOrderId, materialId, status, page, pageSize));
    }

    @GetMapping("/completions/{id}")
    public Result<ProCompletion> getCompletion(@PathVariable Long id) {
        return Result.ok(completionService.getById(id));
    }

    @PostMapping("/completions")
    public Result<ProCompletion> createCompletion(@RequestBody ProCompletion entity) {
        return Result.ok(completionService.create(entity));
    }

    @PutMapping("/completions/{id}")
    public Result<ProCompletion> updateCompletion(@PathVariable Long id, @RequestBody ProCompletion entity) {
        entity.setId(id);
        return Result.ok(completionService.update(entity));
    }

    @DeleteMapping("/completions/{id}")
    public Result<Void> deleteCompletion(@PathVariable Long id) {
        completionService.delete(id);
        return Result.ok();
    }

    // ==================== ProRepair ====================

    @GetMapping("/repairs")
    public Result<PageResult<ProRepair>> listRepairs(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) String repairNo,
                                                      @RequestParam(required = false) Long workOrderId,
                                                      @RequestParam(required = false) Integer status,
                                                      @RequestParam(required = false) Integer repairType,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(repairService.list(keyword != null ? keyword : repairNo, workOrderId, status, repairType, page, pageSize));
    }

    @GetMapping("/repairs/{id}")
    public Result<ProRepair> getRepair(@PathVariable Long id) {
        return Result.ok(repairService.getById(id));
    }

    @PostMapping("/repairs")
    public Result<ProRepair> createRepair(@RequestBody ProRepair entity) {
        return Result.ok(repairService.create(entity));
    }

    @PutMapping("/repairs/{id}")
    public Result<ProRepair> updateRepair(@PathVariable Long id, @RequestBody ProRepair entity) {
        entity.setId(id);
        return Result.ok(repairService.update(entity));
    }

    @DeleteMapping("/repairs/{id}")
    public Result<Void> deleteRepair(@PathVariable Long id) {
        repairService.delete(id);
        return Result.ok();
    }

    // ==================== ProReport ====================

    @GetMapping("/reports")
    public Result<PageResult<ProReport>> listReports(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false) Long orderId,
                                                      @RequestParam(required = false) String reportDate,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(reportService.list(keyword, orderId, reportDate, page, pageSize));
    }

    @GetMapping("/reports/{id}")
    public Result<ProReport> getReport(@PathVariable Long id) {
        return Result.ok(reportService.getById(id));
    }

    @PostMapping("/reports")
    public Result<ProReport> createReport(@RequestBody ProReport entity) {
        return Result.ok(reportService.create(entity));
    }

    @PutMapping("/reports/{id}")
    public Result<ProReport> updateReport(@PathVariable Long id, @RequestBody ProReport entity) {
        entity.setId(id);
        return Result.ok(reportService.update(entity));
    }

    @DeleteMapping("/reports/{id}")
    public Result<Void> deleteReport(@PathVariable Long id) {
        reportService.delete(id);
        return Result.ok();
    }
}
