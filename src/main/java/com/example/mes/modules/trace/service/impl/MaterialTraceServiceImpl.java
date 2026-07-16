package com.example.mes.modules.trace.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.trace.entity.MaterialTrace;
import com.example.mes.modules.trace.mapper.MaterialTraceMapper;
import com.example.mes.modules.trace.service.MaterialTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialTraceServiceImpl implements MaterialTraceService {

    private final MaterialTraceMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.TRACE_MATERIAL_LIST, allEntries = true)
    public MaterialTrace create(MaterialTrace entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.TRACE_MATERIAL, CacheNames.TRACE_MATERIAL_LIST}, allEntries = true)
    public MaterialTrace update(MaterialTrace entity) {
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.TRACE_MATERIAL, CacheNames.TRACE_MATERIAL_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.TRACE_MATERIAL, key = "#id", unless = "#result == null")
    public MaterialTrace getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<MaterialTrace> list(Long orderId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MaterialTrace> rows = mapper.selectList(orderId, offset, pageSize);
        long total = mapper.countList(orderId);
        return PageResult.of(total, page, pageSize, rows);
    }
}
