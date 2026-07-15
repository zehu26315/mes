package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProTask;

public interface ProTaskService {
    ProTask create(ProTask entity);
    ProTask update(ProTask entity);
    void delete(Long id);
    ProTask getById(Long id);
    PageResult<ProTask> list(String taskNo, Long workOrderId, String productionLine,
                              Integer status, Integer sourceType, int page, int pageSize);
}
