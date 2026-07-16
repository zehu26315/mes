package com.example.mes.modules.equipment.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.equipment.entity.EquipInspection;
import com.example.mes.modules.equipment.entity.EquipLedger;
import com.example.mes.modules.equipment.entity.EquipMaintenance;
import com.example.mes.modules.equipment.entity.EquipRepair;
import com.example.mes.modules.equipment.service.EquipInspectionService;
import com.example.mes.modules.equipment.service.EquipLedgerService;
import com.example.mes.modules.equipment.service.EquipMaintenanceService;
import com.example.mes.modules.equipment.service.EquipRepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipLedgerService ledgerService;
    private final EquipInspectionService inspectionService;
    private final EquipMaintenanceService maintenanceService;
    private final EquipRepairService repairService;

    // ==================== Ledger ====================

    @PostMapping("/ledgers")
    public Result<EquipLedger> createLedger(@RequestBody EquipLedger e) {
        return Result.ok(ledgerService.create(e));
    }

    @PutMapping("/ledgers/{id}")
    public Result<EquipLedger> updateLedger(@PathVariable Long id, @RequestBody EquipLedger e) {
        e.setId(id);
        return Result.ok(ledgerService.update(e));
    }

    @DeleteMapping("/ledgers/{id}")
    public Result<Void> deleteLedger(@PathVariable Long id) {
        ledgerService.delete(id);
        return Result.ok();
    }

    @GetMapping("/ledgers/{id}")
    public Result<EquipLedger> getLedger(@PathVariable Long id) {
        return Result.ok(ledgerService.getById(id));
    }

    @GetMapping("/ledgers")
    public Result<PageResult<EquipLedger>> listLedgers(@RequestParam(defaultValue = "") String keyword,
                                                        @RequestParam(required = false) String equipmentCode,
                                                        @RequestParam(required = false) String equipmentName,
                                                        @RequestParam(required = false) String category,
                                                        @RequestParam(required = false) Integer status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(ledgerService.list(equipmentCode, equipmentName, category, status, keyword, page, pageSize));
    }

    // ==================== Inspection ====================

    @PostMapping("/inspections")
    public Result<EquipInspection> createInspection(@RequestBody EquipInspection e) {
        return Result.ok(inspectionService.create(e));
    }

    @PutMapping("/inspections/{id}")
    public Result<EquipInspection> updateInspection(@PathVariable Long id, @RequestBody EquipInspection e) {
        e.setId(id);
        return Result.ok(inspectionService.update(e));
    }

    @DeleteMapping("/inspections/{id}")
    public Result<Void> deleteInspection(@PathVariable Long id) {
        inspectionService.delete(id);
        return Result.ok();
    }

    @GetMapping("/inspections/{id}")
    public Result<EquipInspection> getInspection(@PathVariable Long id) {
        return Result.ok(inspectionService.getById(id));
    }

    @GetMapping("/inspections")
    public Result<PageResult<EquipInspection>> listInspections(@RequestParam(required = false) String keyword,
                                                                @RequestParam(required = false) Long equipmentId,
                                                                @RequestParam(required = false) Integer result,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(inspectionService.list(keyword, equipmentId, result, page, pageSize));
    }

    // ==================== Maintenance ====================

    @PostMapping("/maintenance")
    public Result<EquipMaintenance> createMaintenance(@RequestBody EquipMaintenance e) {
        return Result.ok(maintenanceService.create(e));
    }

    @PutMapping("/maintenance/{id}")
    public Result<EquipMaintenance> updateMaintenance(@PathVariable Long id, @RequestBody EquipMaintenance e) {
        e.setId(id);
        return Result.ok(maintenanceService.update(e));
    }

    @DeleteMapping("/maintenance/{id}")
    public Result<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.delete(id);
        return Result.ok();
    }

    @GetMapping("/maintenance/{id}")
    public Result<EquipMaintenance> getMaintenance(@PathVariable Long id) {
        return Result.ok(maintenanceService.getById(id));
    }

    @GetMapping("/maintenance")
    public Result<PageResult<EquipMaintenance>> listMaintenance(@RequestParam(required = false) String keyword,
                                                                  @RequestParam(required = false) Long equipmentId,
                                                                  @RequestParam(required = false) String maintenanceType,
                                                                  @RequestParam(required = false) Integer status,
                                                                  @RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(maintenanceService.list(keyword, equipmentId, maintenanceType, status, page, pageSize));
    }

    // ==================== Repair ====================

    @GetMapping("/repairs/{id}")
    public Result<EquipRepair> getRepair(@PathVariable Long id) {
        return Result.ok(repairService.getById(id));
    }

    @GetMapping("/repairs")
    public Result<PageResult<EquipRepair>> listRepairs(@RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) Long equipmentId,
                                                        @RequestParam(required = false) Integer status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(repairService.list(keyword, equipmentId, status, page, pageSize));
    }

    @PostMapping("/repairs")
    public Result<EquipRepair> createRepair(@RequestBody EquipRepair e) {
        return Result.ok(repairService.create(e));
    }

    @PutMapping("/repairs/{id}")
    public Result<EquipRepair> updateRepair(@PathVariable Long id, @RequestBody EquipRepair e) {
        e.setId(id);
        return Result.ok(repairService.update(e));
    }

    @DeleteMapping("/repairs/{id}")
    public Result<Void> deleteRepair(@PathVariable Long id) {
        repairService.delete(id);
        return Result.ok();
    }

    @PutMapping("/repairs/{id}/complete")
    public Result<EquipRepair> completeRepair(@PathVariable Long id) {
        return Result.ok(repairService.complete(id));
    }
}
