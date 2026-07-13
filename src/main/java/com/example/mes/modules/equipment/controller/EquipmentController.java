package com.example.mes.modules.equipment.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.equipment.entity.*;
import com.example.mes.modules.equipment.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipCategoryService categoryService;
    private final EquipManufacturerService manufacturerService;
    private final EquipAssetService assetService;
    private final EquipRepairService repairService;
    private final EquipMaintenancePlanService maintenancePlanService;

    // ===== Categories =====
    @GetMapping("/categories")
    public Result<List<TreeNode<EquipCategory>>> categoryTree() { return Result.ok(categoryService.getTree()); }

    @PostMapping("/categories")
    public Result<EquipCategory> createCategory(@RequestBody EquipCategory e) { return Result.ok(categoryService.create(e)); }

    @PutMapping("/categories/{id}")
    public Result<EquipCategory> updateCategory(@PathVariable Long id, @RequestBody EquipCategory e) { e.setId(id); return Result.ok(categoryService.update(e)); }

    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) { categoryService.delete(id); return Result.ok(); }

    @GetMapping("/categories/{id}")
    public Result<EquipCategory> getCategory(@PathVariable Long id) { return Result.ok(categoryService.getById(id)); }

    // ===== Manufacturers =====
    @GetMapping("/manufacturers")
    public Result<List<EquipManufacturer>> listManufacturers() { return Result.ok(manufacturerService.listAll()); }

    @PostMapping("/manufacturers")
    public Result<EquipManufacturer> createManufacturer(@RequestBody EquipManufacturer e) { return Result.ok(manufacturerService.create(e)); }

    @PutMapping("/manufacturers/{id}")
    public Result<EquipManufacturer> updateManufacturer(@PathVariable Long id, @RequestBody EquipManufacturer e) { e.setId(id); return Result.ok(manufacturerService.update(e)); }

    @DeleteMapping("/manufacturers/{id}")
    public Result<Void> deleteManufacturer(@PathVariable Long id) { manufacturerService.delete(id); return Result.ok(); }

    // ===== Assets =====
    @GetMapping("/assets")
    public Result<PageResult<EquipAsset>> listAssets(@RequestParam(defaultValue = "") String keyword,
                                                      @RequestParam(required = false) String status,
                                                      @RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String productionLine,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(assetService.list(keyword, status, categoryId, productionLine, page, pageSize));
    }

    @PostMapping("/assets")
    public Result<EquipAsset> createAsset(@RequestBody EquipAsset e) { return Result.ok(assetService.create(e)); }

    @PutMapping("/assets/{id}")
    public Result<EquipAsset> updateAsset(@PathVariable Long id, @RequestBody EquipAsset e) { e.setId(id); return Result.ok(assetService.update(e)); }

    @DeleteMapping("/assets/{id}")
    public Result<Void> deleteAsset(@PathVariable Long id) { assetService.delete(id); return Result.ok(); }

    @GetMapping("/assets/{id}")
    public Result<EquipAsset> getAsset(@PathVariable Long id) { return Result.ok(assetService.getById(id)); }

    @PutMapping("/assets/{id}/status")
    public Result<EquipAsset> updateAssetStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return Result.ok(assetService.updateStatus(id, body.get("status"), body.get("reason"), body.get("operator")));
    }

    // ===== Maintenance Plans =====
    @GetMapping("/maintenance-plans")
    public Result<PageResult<EquipMaintenancePlan>> listMaintenancePlans(@RequestParam(required = false) Long equipId,
                                                                          @RequestParam(required = false) String status,
                                                                          @RequestParam(required = false) String startDate,
                                                                          @RequestParam(required = false) String endDate,
                                                                          @RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(maintenancePlanService.list(equipId, status, startDate, endDate, page, pageSize));
    }

    @PostMapping("/maintenance-plans")
    public Result<EquipMaintenancePlan> createMaintenancePlan(@RequestBody EquipMaintenancePlan e) { return Result.ok(maintenancePlanService.create(e)); }

    @PostMapping("/maintenance-records")
    public Result<EquipMaintenanceRecord> createMaintenanceRecord(@RequestBody EquipMaintenanceRecord e) { return Result.ok(maintenancePlanService.createRecord(e)); }

    // ===== Repairs =====
    @GetMapping("/repairs")
    public Result<PageResult<EquipRepair>> listRepairs(@RequestParam(required = false) Long equipId,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(repairService.list(equipId, status, page, pageSize));
    }

    @PostMapping("/repairs")
    public Result<EquipRepair> createRepair(@RequestBody EquipRepair e) { return Result.ok(repairService.create(e)); }

    @PutMapping("/repairs/{id}/complete")
    public Result<EquipRepair> completeRepair(@PathVariable Long id) { return Result.ok(repairService.complete(id)); }
}
