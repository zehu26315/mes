package com.example.mes.modules.equipment.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipLedger;

public interface EquipLedgerService {

    EquipLedger create(EquipLedger entity);

    EquipLedger update(EquipLedger entity);

    void delete(Long id);

    EquipLedger getById(Long id);

    PageResult<EquipLedger> list(String equipmentCode, String equipmentName, String category,
                                  Integer status, String keyword, int page, int pageSize);
}
