package com.example.mes.modules.warehouse.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.warehouse.entity.*;
import com.example.mes.modules.warehouse.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WhWarehouseService warehouseService;
    private final WhInboundService inboundService;
    private final WhOutboundService outboundService;
    private final WhTransferService transferService;
    private final WhCheckService checkService;

    // ===== Warehouse CRUD =====
    @GetMapping("/warehouses")
    public Result<List<WhWarehouse>> listAll() { return Result.ok(warehouseService.listAll()); }

    @PostMapping("/warehouses")
    public Result<WhWarehouse> create(@RequestBody WhWarehouse entity) { return Result.ok(warehouseService.create(entity)); }

    @PutMapping("/warehouses/{id}")
    public Result<WhWarehouse> update(@PathVariable Long id, @RequestBody WhWarehouse entity) { entity.setId(id); return Result.ok(warehouseService.update(entity)); }

    @DeleteMapping("/warehouses/{id}")
    public Result<Void> delete(@PathVariable Long id) { warehouseService.delete(id); return Result.ok(); }

    @GetMapping("/warehouses/{id}")
    public Result<WhWarehouse> getById(@PathVariable Long id) { return Result.ok(warehouseService.getById(id)); }

    // ===== Inbound =====
    @GetMapping("/inbounds")
    public Result<PageResult<WhInbound>> listInbounds(@RequestParam(required = false) String type,
                                                       @RequestParam(required = false) Long warehouseId,
                                                       @RequestParam(required = false) String status,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(inboundService.list(type, warehouseId, status, page, pageSize));
    }

    @PostMapping("/inbounds")
    public Result<WhInbound> createInbound(@RequestBody WhInbound entity) { return Result.ok(inboundService.create(entity)); }

    @GetMapping("/inbounds/{id}")
    public Result<WhInbound> getInbound(@PathVariable Long id) { return Result.ok(inboundService.getById(id)); }

    // ===== Outbound =====
    @GetMapping("/outbounds")
    public Result<PageResult<WhOutbound>> listOutbounds(@RequestParam(required = false) String type,
                                                         @RequestParam(required = false) Long warehouseId,
                                                         @RequestParam(required = false) String status,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(outboundService.list(type, warehouseId, status, page, pageSize));
    }

    @PostMapping("/outbounds")
    public Result<WhOutbound> createOutbound(@RequestBody WhOutbound entity) { return Result.ok(outboundService.create(entity)); }

    @GetMapping("/outbounds/{id}")
    public Result<WhOutbound> getOutbound(@PathVariable Long id) { return Result.ok(outboundService.getById(id)); }

    // ===== Transfer =====
    @GetMapping("/transfers")
    public Result<PageResult<WhTransfer>> listTransfers(@RequestParam(required = false) String status,
                                                         @RequestParam(required = false) Long fromWarehouseId,
                                                         @RequestParam(required = false) Long toWarehouseId,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(transferService.list(status, fromWarehouseId, toWarehouseId, page, pageSize));
    }

    @PostMapping("/transfers")
    public Result<WhTransfer> createTransfer(@RequestBody WhTransfer entity) { return Result.ok(transferService.create(entity)); }

    @GetMapping("/transfers/{id}")
    public Result<WhTransfer> getTransfer(@PathVariable Long id) { return Result.ok(transferService.getById(id)); }

    // ===== Check =====
    @GetMapping("/checks")
    public Result<PageResult<WhCheck>> listChecks(@RequestParam(required = false) Long warehouseId,
                                                   @RequestParam(required = false) String status,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(checkService.list(warehouseId, status, page, pageSize));
    }

    @PostMapping("/checks")
    public Result<WhCheck> createCheck(@RequestBody WhCheck entity) { return Result.ok(checkService.create(entity)); }

    @PutMapping("/checks/{id}/confirm")
    public Result<WhCheck> confirmCheck(@PathVariable Long id) { return Result.ok(checkService.confirm(id)); }

    @GetMapping("/checks/{id}")
    public Result<WhCheck> getCheck(@PathVariable Long id) { return Result.ok(checkService.getById(id)); }
}
