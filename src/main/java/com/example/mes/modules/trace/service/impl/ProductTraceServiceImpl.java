package com.example.mes.modules.trace.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.trace.entity.ProductTrace;
import com.example.mes.modules.trace.mapper.ProductTraceMapper;
import com.example.mes.modules.trace.service.ProductTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTraceServiceImpl implements ProductTraceService {

    private final ProductTraceMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.TRACE_PRODUCT_LIST, allEntries = true)
    public ProductTrace create(ProductTrace entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.TRACE_PRODUCT, CacheNames.TRACE_PRODUCT_LIST}, allEntries = true)
    public ProductTrace update(ProductTrace entity) {
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.TRACE_PRODUCT, CacheNames.TRACE_PRODUCT_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.TRACE_PRODUCT, key = "#id", unless = "#result == null")
    public ProductTrace getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<ProductTrace> list(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProductTrace> rows = mapper.selectList(keyword, offset, pageSize);
        long total = mapper.countList(keyword);
        return PageResult.of(total, page, pageSize, rows);
    }
}
