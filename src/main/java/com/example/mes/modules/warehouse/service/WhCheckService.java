package com.example.mes.modules.warehouse.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhCheck;

public interface WhCheckService {
    WhCheck create(WhCheck entity);
    WhCheck confirm(Long id);
    PageResult<WhCheck> list(Long warehouseId, String status, int page, int pageSize);
    WhCheck getById(Long id);
}
