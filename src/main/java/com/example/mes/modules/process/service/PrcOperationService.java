package com.example.mes.modules.process.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.process.entity.PrcOperation;

public interface PrcOperationService {
    PrcOperation create(PrcOperation entity);
    PrcOperation update(PrcOperation entity);
    void delete(Long id);
    PrcOperation getById(Long id);
    PageResult<PrcOperation> list(String operationCode, String operationName, Integer operationType,
                                   Integer status, String keyword, int page, int pageSize);
}
