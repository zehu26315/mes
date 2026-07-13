package com.example.mes.modules.equipment.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipMaintenancePlan;
import com.example.mes.modules.equipment.entity.EquipMaintenanceRecord;

public interface EquipMaintenancePlanService {
    EquipMaintenancePlan create(EquipMaintenancePlan e);
    EquipMaintenancePlan getById(Long id);
    PageResult<EquipMaintenancePlan> list(Long equipId, String status, String startDate, String endDate, int page, int pageSize);
    EquipMaintenanceRecord createRecord(EquipMaintenanceRecord record);
}
