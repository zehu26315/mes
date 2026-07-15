package com.example.mes.modules.process.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.process.entity.PrcSop;
import com.example.mes.modules.process.mapper.PrcSopMapper;
import com.example.mes.modules.process.service.PrcSopService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrcSopServiceImpl implements PrcSopService {

    private final PrcSopMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PRC_SOP_LIST, allEntries = true)
    public PrcSop create(PrcSop entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getSortOrder() == null) entity.setSortOrder(0);
        if (entity.getAutoPlay() == null) entity.setAutoPlay(0);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_SOP, CacheNames.PRC_SOP_LIST}, allEntries = true)
    public PrcSop update(PrcSop entity) {
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_SOP, CacheNames.PRC_SOP_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.PRC_SOP, key = "#id", unless = "#result == null")
    public PrcSop getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<PrcSop> list(Long operationId, String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PrcSop> rows = mapper.selectList(operationId, keyword, offset, pageSize);
        long total = mapper.countList(operationId, keyword);
        return PageResult.of(total, page, pageSize, rows);
    }
}
