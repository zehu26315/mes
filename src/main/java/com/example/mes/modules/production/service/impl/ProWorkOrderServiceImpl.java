package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.production.entity.ProWorkOrder;
import com.example.mes.modules.production.mapper.ProWorkOrderMapper;
import com.example.mes.modules.production.service.ProWorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProWorkOrderServiceImpl implements ProWorkOrderService {

    private final ProWorkOrderMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PROD_WORK_ORDER_LIST, allEntries = true)
    public ProWorkOrder create(ProWorkOrder entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        if (entity.getPriority() == null) entity.setPriority(0);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_WORK_ORDER, CacheNames.PROD_WORK_ORDER_LIST}, allEntries = true)
    public ProWorkOrder update(ProWorkOrder entity) {
        entity.setWorkOrderNo(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_WORK_ORDER, CacheNames.PROD_WORK_ORDER_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROD_WORK_ORDER, key = "#id", unless = "#result == null")
    public ProWorkOrder getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<ProWorkOrder> list(String workOrderNo, Long materialId, Integer status,
                                          Integer sourceType, Integer priority, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProWorkOrder> rows = mapper.selectList(workOrderNo, materialId, status, sourceType, priority, offset, pageSize);
        long total = mapper.countList(workOrderNo, materialId, status, sourceType, priority);
        return PageResult.of(total, page, pageSize, rows);
    }
}
