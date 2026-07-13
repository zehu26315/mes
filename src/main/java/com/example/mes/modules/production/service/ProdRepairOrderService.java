package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdRepairOrder;

public interface ProdRepairOrderService {

    ProdRepairOrder create(ProdRepairOrder repairOrder);

    ProdRepairOrder getById(Long id);

    PageResult<ProdRepairOrder> list(int page, int pageSize);

    void complete(Long id, String result);
}
