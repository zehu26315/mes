package com.example.mes.modules.process.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.process.entity.PrcRouteDetail;

public interface PrcRouteDetailService {
    PrcRouteDetail create(PrcRouteDetail entity);
    PrcRouteDetail update(PrcRouteDetail entity);
    void delete(Long id);
    PrcRouteDetail getById(Long id);
    PageResult<PrcRouteDetail> list(Long routeId, int page, int pageSize);
}
