package com.example.mes.modules.process.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.process.entity.PrcRoute;

public interface PrcRouteService {
    PrcRoute create(PrcRoute entity);
    PrcRoute update(PrcRoute entity);
    void delete(Long id);
    PrcRoute getById(Long id);
    PageResult<PrcRoute> list(String routeCode, String routeName, String keyword,
                               Long materialId, Integer status, int page, int pageSize);
}
