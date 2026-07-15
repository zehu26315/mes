package com.example.mes.modules.equipment.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipMaintenance;

public interface EquipMaintenanceService {

    EquipMaintenance create(EquipMaintenance e);

    EquipMaintenance update(EquipMaintenance e);

    void delete(Long id);

    EquipMaintenance getById(Long id);

    PageResult<EquipMaintenance> list(String keyword, Long equipmentId, String maintenanceType, Integer status, int page, int pageSize);
}
