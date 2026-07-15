package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProWorkOrder;

public interface ProWorkOrderService {
    ProWorkOrder create(ProWorkOrder entity);
    ProWorkOrder update(ProWorkOrder entity);
    void delete(Long id);
    ProWorkOrder getById(Long id);
    PageResult<ProWorkOrder> list(String workOrderNo, Long materialId, Integer status,
                                   Integer sourceType, Integer priority, int page, int pageSize);
}
