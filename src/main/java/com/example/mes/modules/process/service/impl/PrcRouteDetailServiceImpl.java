package com.example.mes.modules.process.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.process.entity.PrcRouteDetail;
import com.example.mes.modules.process.mapper.PrcRouteDetailMapper;
import com.example.mes.modules.process.service.PrcRouteDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrcRouteDetailServiceImpl implements PrcRouteDetailService {

    private final PrcRouteDetailMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PRC_ROUTE_DETAIL_LIST, allEntries = true)
    public PrcRouteDetail create(PrcRouteDetail entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_ROUTE_DETAIL, CacheNames.PRC_ROUTE_DETAIL_LIST}, allEntries = true)
    public PrcRouteDetail update(PrcRouteDetail entity) {
        entity.setRouteId(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_ROUTE_DETAIL, CacheNames.PRC_ROUTE_DETAIL_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.PRC_ROUTE_DETAIL, key = "#id", unless = "#result == null")
    public PrcRouteDetail getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<PrcRouteDetail> list(Long routeId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PrcRouteDetail> rows = mapper.selectList(routeId, offset, pageSize);
        long total = mapper.countList(routeId);
        return PageResult.of(total, page, pageSize, rows);
    }
}
