package com.example.mes.modules.process.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.process.entity.ProcessRoute;
import com.example.mes.modules.process.mapper.ProcessRouteMapper;
import com.example.mes.modules.process.service.ProcessRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessRouteServiceImpl implements ProcessRouteService {

    private final ProcessRouteMapper processRouteMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PROCESS_ROUTE, CacheNames.PROCESS_ROUTE_LIST}, allEntries = true)
    public ProcessRoute createRoute(ProcessRoute route) {
        if (route.getStatus() == null) {
            route.setStatus(1);
        }
        processRouteMapper.insert(route);
        return route;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PROCESS_ROUTE, CacheNames.PROCESS_ROUTE_LIST}, allEntries = true)
    public ProcessRoute updateRoute(ProcessRoute route) {
        route.setRouteCode(null);
        processRouteMapper.update(route);
        return processRouteMapper.selectById(route.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PROCESS_ROUTE, CacheNames.PROCESS_ROUTE_LIST}, allEntries = true)
    public void deleteRoute(Long id) {
        processRouteMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROCESS_ROUTE, key = "#id", unless = "#result == null")
    public ProcessRoute getRouteById(Long id) {
        return processRouteMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessRoute> listRoutes(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProcessRoute> rows = processRouteMapper.selectList(keyword, status, offset, pageSize);
        long total = processRouteMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
