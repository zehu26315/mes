package com.example.mes.modules.equipment.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipRepair;

public interface EquipRepairService {
    EquipRepair create(EquipRepair e);
    EquipRepair update(EquipRepair e);
    void delete(Long id);
    EquipRepair getById(Long id);
    PageResult<EquipRepair> list(String keyword, Long equipmentId, Integer status, int page, int pageSize);
    EquipRepair complete(Long id);
}
