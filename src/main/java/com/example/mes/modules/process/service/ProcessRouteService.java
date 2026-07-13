package com.example.mes.modules.process.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.process.entity.ProcessRoute;

public interface ProcessRouteService {

    ProcessRoute createRoute(ProcessRoute route);

    ProcessRoute updateRoute(ProcessRoute route);

    void deleteRoute(Long id);

    ProcessRoute getRouteById(Long id);

    PageResult<ProcessRoute> listRoutes(String keyword, Integer status, int page, int pageSize);
}
