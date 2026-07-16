package com.example.mes.modules.process.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.process.entity.*;
import com.example.mes.modules.process.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessController {

    private final PrcOperationService operationService;
    private final PrcRouteService routeService;
    private final PrcRouteDetailService routeDetailService;
    private final PrcSopService sopService;

    // ==================== Operation ====================

    @GetMapping("/operations")
    public Result<?> listOperations(@RequestParam(required = false) String operationCode,
                                     @RequestParam(required = false) String operationName,
                                     @RequestParam(required = false) Integer operationType,
                                     @RequestParam(required = false) Integer status,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(operationService.list(operationCode, operationName, operationType, status, keyword, page, pageSize));
    }

    @GetMapping("/operations/{id}")
    public Result<PrcOperation> getOperation(@PathVariable Long id) {
        return Result.ok(operationService.getById(id));
    }

    @PostMapping("/operations")
    public Result<PrcOperation> createOperation(@RequestBody PrcOperation entity) {
        return Result.ok(operationService.create(entity));
    }

    @PutMapping("/operations/{id}")
    public Result<PrcOperation> updateOperation(@PathVariable Long id, @RequestBody PrcOperation entity) {
        entity.setId(id);
        return Result.ok(operationService.update(entity));
    }

    @DeleteMapping("/operations/{id}")
    public Result<Void> deleteOperation(@PathVariable Long id) {
        operationService.delete(id);
        return Result.ok();
    }

    // ==================== Route ====================

    @GetMapping("/routes")
    public Result<?> listRoutes(@RequestParam(required = false) String routeCode,
                                 @RequestParam(required = false) String routeName,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) Long materialId,
                                 @RequestParam(required = false) Integer status,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(routeService.list(routeCode, routeName, keyword, materialId, status, page, pageSize));
    }

    @GetMapping("/routes/{id}")
    public Result<PrcRoute> getRoute(@PathVariable Long id) {
        return Result.ok(routeService.getById(id));
    }

    @PostMapping("/routes")
    public Result<PrcRoute> createRoute(@RequestBody PrcRoute entity) {
        return Result.ok(routeService.create(entity));
    }

    @PutMapping("/routes/{id}")
    public Result<PrcRoute> updateRoute(@PathVariable Long id, @RequestBody PrcRoute entity) {
        entity.setId(id);
        return Result.ok(routeService.update(entity));
    }

    @DeleteMapping("/routes/{id}")
    public Result<Void> deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
        return Result.ok();
    }

    // ==================== RouteDetail ====================

    @GetMapping("/route-details/{id}")
    public Result<PrcRouteDetail> getRouteDetail(@PathVariable Long id) {
        return Result.ok(routeDetailService.getById(id));
    }

    @GetMapping("/route-details")
    public Result<PageResult<PrcRouteDetail>> listRouteDetails(@RequestParam(required = false) Long routeId,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(routeDetailService.list(routeId, page, pageSize));
    }

    @PostMapping("/route-details")
    public Result<PrcRouteDetail> createRouteDetail(@RequestBody PrcRouteDetail entity) {
        return Result.ok(routeDetailService.create(entity));
    }

    @PutMapping("/route-details/{id}")
    public Result<PrcRouteDetail> updateRouteDetail(@PathVariable Long id, @RequestBody PrcRouteDetail entity) {
        entity.setId(id);
        return Result.ok(routeDetailService.update(entity));
    }

    @DeleteMapping("/route-details/{id}")
    public Result<Void> deleteRouteDetail(@PathVariable Long id) {
        routeDetailService.delete(id);
        return Result.ok();
    }

    // ==================== SOP ====================

    @GetMapping("/sops/{id}")
    public Result<PrcSop> getSop(@PathVariable Long id) {
        return Result.ok(sopService.getById(id));
    }

    @GetMapping("/sops")
    public Result<PageResult<PrcSop>> listSops(@RequestParam(required = false) Long operationId,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(sopService.list(operationId, keyword, page, pageSize));
    }

    @PostMapping("/sops")
    public Result<PrcSop> createSop(@RequestBody PrcSop entity) {
        return Result.ok(sopService.create(entity));
    }

    @PutMapping("/sops/{id}")
    public Result<PrcSop> updateSop(@PathVariable Long id, @RequestBody PrcSop entity) {
        entity.setId(id);
        return Result.ok(sopService.update(entity));
    }

    @DeleteMapping("/sops/{id}")
    public Result<Void> deleteSop(@PathVariable Long id) {
        sopService.delete(id);
        return Result.ok();
    }
}
