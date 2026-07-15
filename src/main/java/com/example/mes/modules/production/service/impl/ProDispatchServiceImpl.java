package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.production.entity.ProDispatch;
import com.example.mes.modules.production.mapper.ProDispatchMapper;
import com.example.mes.modules.production.service.ProDispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProDispatchServiceImpl implements ProDispatchService {

    private final ProDispatchMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PROD_DISPATCH_LIST, allEntries = true)
    public ProDispatch create(ProDispatch entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_DISPATCH, CacheNames.PROD_DISPATCH_LIST}, allEntries = true)
    public ProDispatch update(ProDispatch entity) {
        entity.setDispatchNo(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_DISPATCH, CacheNames.PROD_DISPATCH_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROD_DISPATCH, key = "#id", unless = "#result == null")
    public ProDispatch getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<ProDispatch> list(String dispatchNo, Long workOrderId, String productionLine,
                                         Integer status, Long assigneeId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProDispatch> rows = mapper.selectList(dispatchNo, workOrderId, productionLine, status, assigneeId, offset, pageSize);
        long total = mapper.countList(dispatchNo, workOrderId, productionLine, status, assigneeId);
        return PageResult.of(total, page, pageSize, rows);
    }
}
