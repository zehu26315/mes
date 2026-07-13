package com.example.mes.modules.equipment.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipAsset;

public interface EquipAssetService {
    EquipAsset create(EquipAsset e);
    EquipAsset update(EquipAsset e);
    void delete(Long id);
    EquipAsset getById(Long id);
    PageResult<EquipAsset> list(String keyword, String status, Long categoryId, String productionLine, int page, int pageSize);
    EquipAsset updateStatus(Long id, String status, String reason, String operator);
}
