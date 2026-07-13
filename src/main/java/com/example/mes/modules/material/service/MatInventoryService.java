package com.example.mes.modules.material.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatInventory;

public interface MatInventoryService {

    PageResult<MatInventory> list(Long materialId, String materialCode, String warehouse, int page, int pageSize);

    PageResult<MatInventory> getLowStock(int page, int pageSize);
}
