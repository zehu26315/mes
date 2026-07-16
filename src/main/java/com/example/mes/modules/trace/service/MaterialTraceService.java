package com.example.mes.modules.trace.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.trace.entity.MaterialTrace;

public interface MaterialTraceService {
    MaterialTrace create(MaterialTrace entity);
    MaterialTrace update(MaterialTrace entity);
    void delete(Long id);
    MaterialTrace getById(Long id);
    PageResult<MaterialTrace> list(Long orderId, int page, int pageSize);
}
