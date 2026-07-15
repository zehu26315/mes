package com.example.mes.modules.process.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.process.entity.PrcRoute;
import com.example.mes.modules.process.mapper.PrcRouteMapper;
import com.example.mes.modules.process.service.PrcRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrcRouteServiceImpl implements PrcRouteService {

    private final PrcRouteMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PRC_ROUTE_LIST, allEntries = true)
    public PrcRoute create(PrcRoute entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_ROUTE, CacheNames.PRC_ROUTE_LIST}, allEntries = true)
    public PrcRoute update(PrcRoute entity) {
        entity.setRouteCode(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_ROUTE, CacheNames.PRC_ROUTE_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.PRC_ROUTE, key = "#id", unless = "#result == null")
    public PrcRoute getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<PrcRoute> list(String routeCode, String routeName, String keyword,
                                      Long materialId, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PrcRoute> rows = mapper.selectList(routeCode, routeName, keyword, materialId, status, offset, pageSize);
        long total = mapper.countList(routeCode, routeName, keyword, materialId, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
