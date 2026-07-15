package com.example.mes.modules.equipment.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipInspection;

public interface EquipInspectionService {

    EquipInspection create(EquipInspection e);

    EquipInspection update(EquipInspection e);

    void delete(Long id);

    EquipInspection getById(Long id);

    PageResult<EquipInspection> list(String keyword, Long equipmentId, Integer result, int page, int pageSize);
}
