package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.production.entity.ProCompletion;
import com.example.mes.modules.production.mapper.ProCompletionMapper;
import com.example.mes.modules.production.service.ProCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProCompletionServiceImpl implements ProCompletionService {

    private final ProCompletionMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PROD_COMPLETION_LIST, allEntries = true)
    public ProCompletion create(ProCompletion entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_COMPLETION, CacheNames.PROD_COMPLETION_LIST}, allEntries = true)
    public ProCompletion update(ProCompletion entity) {
        entity.setCompletionNo(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_COMPLETION, CacheNames.PROD_COMPLETION_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROD_COMPLETION, key = "#id", unless = "#result == null")
    public ProCompletion getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<ProCompletion> list(String completionNo, Long workOrderId, Long materialId,
                                           Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProCompletion> rows = mapper.selectList(completionNo, workOrderId, materialId, status, offset, pageSize);
        long total = mapper.countList(completionNo, workOrderId, materialId, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
