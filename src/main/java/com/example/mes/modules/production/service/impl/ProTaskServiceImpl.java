package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.production.entity.ProTask;
import com.example.mes.modules.production.mapper.ProTaskMapper;
import com.example.mes.modules.production.service.ProTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProTaskServiceImpl implements ProTaskService {

    private final ProTaskMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PROD_TASK_LIST, allEntries = true)
    public ProTask create(ProTask entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_TASK, CacheNames.PROD_TASK_LIST}, allEntries = true)
    public ProTask update(ProTask entity) {
        entity.setTaskNo(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_TASK, CacheNames.PROD_TASK_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROD_TASK, key = "#id", unless = "#result == null")
    public ProTask getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<ProTask> list(String taskNo, Long workOrderId, String productionLine,
                                     Integer status, Integer sourceType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProTask> rows = mapper.selectList(taskNo, workOrderId, productionLine, status, sourceType, offset, pageSize);
        long total = mapper.countList(taskNo, workOrderId, productionLine, status, sourceType);
        return PageResult.of(total, page, pageSize, rows);
    }
}
