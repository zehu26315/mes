package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProDispatch;

public interface ProDispatchService {
    ProDispatch create(ProDispatch entity);
    ProDispatch update(ProDispatch entity);
    void delete(Long id);
    ProDispatch getById(Long id);
    PageResult<ProDispatch> list(String dispatchNo, Long workOrderId, String productionLine,
                                  Integer status, Long assigneeId, int page, int pageSize);
}
