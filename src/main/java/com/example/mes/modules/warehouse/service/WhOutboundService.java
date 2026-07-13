package com.example.mes.modules.warehouse.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhOutbound;

public interface WhOutboundService {
    WhOutbound create(WhOutbound entity);
    PageResult<WhOutbound> list(String type, Long warehouseId, String status, int page, int pageSize);
    WhOutbound getById(Long id);
}
