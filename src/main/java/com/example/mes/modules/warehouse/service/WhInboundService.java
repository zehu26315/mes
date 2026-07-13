package com.example.mes.modules.warehouse.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhInbound;

public interface WhInboundService {
    WhInbound create(WhInbound entity);
    PageResult<WhInbound> list(String type, Long warehouseId, String status, int page, int pageSize);
    WhInbound getById(Long id);
}
