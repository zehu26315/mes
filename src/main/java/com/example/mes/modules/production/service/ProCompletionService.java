package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProCompletion;

public interface ProCompletionService {
    ProCompletion create(ProCompletion entity);
    ProCompletion update(ProCompletion entity);
    void delete(Long id);
    ProCompletion getById(Long id);
    PageResult<ProCompletion> list(String completionNo, Long workOrderId, Long materialId,
                                    Integer status, int page, int pageSize);
}
