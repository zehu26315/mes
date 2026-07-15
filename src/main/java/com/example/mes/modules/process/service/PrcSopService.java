package com.example.mes.modules.process.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.process.entity.PrcSop;

public interface PrcSopService {
    PrcSop create(PrcSop entity);
    PrcSop update(PrcSop entity);
    void delete(Long id);
    PrcSop getById(Long id);
    PageResult<PrcSop> list(Long operationId, String keyword, int page, int pageSize);
}
